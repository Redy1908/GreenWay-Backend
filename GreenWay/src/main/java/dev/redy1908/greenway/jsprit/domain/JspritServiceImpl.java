package dev.redy1908.greenway.jsprit.domain;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.algorithm.state.StateId;
import com.graphhopper.jsprit.core.algorithm.state.StateManager;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.constraint.ConstraintManager;
import com.graphhopper.jsprit.core.problem.constraint.HardActivityConstraint;
import com.graphhopper.jsprit.core.problem.cost.VehicleRoutingTransportCosts;
import com.graphhopper.jsprit.core.problem.job.Shipment;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.PickupShipment;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.core.util.VehicleRoutingTransportCostsMatrix;
import dev.redy1908.greenway.delivery.domain.Delivery;
import dev.redy1908.greenway.delivery.domain.IDeliveryService;
import dev.redy1908.greenway.delivery_man.domain.DeliveryMan;
import dev.redy1908.greenway.delivery_man.domain.IDeliveryManService;
import dev.redy1908.greenway.delivery_vehicle.domain.DeliveryVehicle;
import dev.redy1908.greenway.delivery_vehicle.domain.IDeliveryVehicleService;
import dev.redy1908.greenway.jsprit.domain.exceptions.models.NoDeliveryManToOrganizeException;
import dev.redy1908.greenway.jsprit.domain.exceptions.models.NoDeliveryToOrganizeException;
import dev.redy1908.greenway.jsprit.domain.exceptions.models.NoDeliveryVehicleToOrganizeException;
import dev.redy1908.greenway.osrm.domain.IOsrmService;
import dev.redy1908.greenway.osrm.domain.NavigationType;
import dev.redy1908.greenway.trip.ITripService;
import dev.redy1908.greenway.trip.Trip;
import dev.redy1908.greenway.vehicle_deposit.domain.IVehicleDepositService;
import dev.redy1908.greenway.vehicle_deposit.domain.VehicleDeposit;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class JspritServiceImpl implements IJspritService {

    private static final int DEPOSIT_LOCATION = 0;
    private static final int AUTONOMY_IN_METER_INDEX = 0;
    private static final int WEIGHT_IN_KG_INDEX = 1;
    private static final int TURN_START_TIME_SECONDS = 9 * 60 * 60;
    private static final int TURN_END_TIME_SECONDS = 17 * 60 * 60;
    private static final int EST_CLIENT_RETRIEVE_TIME_SECONDS = 7 * 60;
    private static final String DISTANCE_STATE_NAME = "distance";
    private final IOsrmService osrmService;
    private final IDeliveryVehicleService deliveryVehicleService;
    private final IDeliveryService deliveryService;
    private final IDeliveryManService deliveryManService;
    private final IVehicleDepositService depositService;
    private final ITripService tripService;
    private List<Delivery> deliveryList;
    private List<DeliveryVehicle> vehicleList;
    private List<DeliveryMan> deliveryManList;
    private Pair<double[][], double[][]> costMatrices;

    //@Scheduled(cron = "0 0 6 * * ?")
    public String schedule() {

        loadData();

        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
        vrpBuilder.setFleetSize(VehicleRoutingProblem.FleetSize.FINITE);

        addVehicles(vrpBuilder, vehicleList);
        addDeliveries(vrpBuilder, deliveryList);
        setRoutingCosts(vrpBuilder, costMatrices);

        VehicleRoutingProblem vrp = vrpBuilder.build();

        StateManager stateManager = buildStateManager(vrp);
        ConstraintManager constraintManager = buildConstraintManager(vrp, stateManager);

        addMaxDistanceConstraint(vrp, stateManager, constraintManager);

        VehicleRoutingAlgorithm vra = Jsprit.Builder
                .newInstance(vrp)
                .setStateAndConstraintManager(stateManager, constraintManager)
                .buildAlgorithm();

        Collection<VehicleRoutingProblemSolution> solutions = vra.searchSolutions();

        VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);

        organize(bestSolution);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        SolutionPrinter.print(printWriter, vrp, bestSolution, SolutionPrinter.Print.VERBOSE);

        return stringWriter.toString();
    }

    private void loadData() {
        deliveryList = deliveryService.findUnassignedDeliveries();
        vehicleList = deliveryVehicleService.findAllEmptyVehicles();
        deliveryManList = deliveryManService.findFreeDeliveryMen();
        VehicleDeposit vehicleDeposit = depositService.getVehicleDeposit();

        if (deliveryList.isEmpty()) {
            throw new NoDeliveryToOrganizeException();
        }

        if (deliveryManList.isEmpty()) {
            throw new NoDeliveryManToOrganizeException();
        }

        if (vehicleList.isEmpty()) {
            throw new NoDeliveryVehicleToOrganizeException();
        }

        costMatrices = osrmService.getMatrixDistances(vehicleDeposit, deliveryList);
    }

    private void addVehicles(VehicleRoutingProblem.Builder vrpBuilder, List<DeliveryVehicle> deliveryVehicleList) {


        for (DeliveryVehicle deliveryVehicle : deliveryVehicleList) {
            String id = deliveryVehicle.getId().toString();

            VehicleType type = VehicleTypeImpl.Builder.newInstance(id)
                    .addCapacityDimension(AUTONOMY_IN_METER_INDEX, deliveryVehicle.getMaxAutonomyKm() * 1000)
                    .addCapacityDimension(WEIGHT_IN_KG_INDEX, deliveryVehicle.getMaxCapacityKg() - deliveryVehicle.getCurrentLoadKg())
                    .build();

            VehicleImpl vehicle = VehicleImpl.Builder.newInstance(id)
                    .setStartLocation(Location.newInstance(DEPOSIT_LOCATION))
                    .setEarliestStart(TURN_START_TIME_SECONDS)
                    .setLatestArrival(TURN_END_TIME_SECONDS)
                    .setReturnToDepot(true)
                    .setType(type)
                    .build();

            vrpBuilder.addVehicle(vehicle);
        }
    }

    private void addDeliveries(VehicleRoutingProblem.Builder vrpBuilder, List<Delivery> deliveryList) {

        int pos = 1;
        for (Delivery delivery : deliveryList) {
            String id = delivery.getId().toString();

            Shipment service = Shipment.Builder.newInstance(id)
                    .setPickupLocation(Location.newInstance(DEPOSIT_LOCATION))
                    .setDeliveryLocation(Location.newInstance(pos))
                    .setDeliveryServiceTime(EST_CLIENT_RETRIEVE_TIME_SECONDS)
                    .addSizeDimension(WEIGHT_IN_KG_INDEX, delivery.getWeightKg())
                    .build();

            vrpBuilder.addJob(service);
            pos++;
        }
    }

    private void setRoutingCosts(VehicleRoutingProblem.Builder vrpBuilder, Pair<double[][], double[][]> costMatrices) {

        VehicleRoutingTransportCostsMatrix.Builder costMatrixBuilder = VehicleRoutingTransportCostsMatrix.Builder.newInstance(false);

        double[][] matrixDurations = costMatrices.getFirst();
        double[][] matrixDistances = costMatrices.getSecond();

        int matrixSize = matrixDurations.length;

        for (int from = 0; from < matrixSize; from++) {
            for (int to = 0; to < matrixSize; to++) {
                costMatrixBuilder.addTransportDistance(Integer.toString(from), Integer.toString(to), matrixDistances[from][to]);
                costMatrixBuilder.addTransportTime(Integer.toString(from), Integer.toString(to), matrixDurations[from][to]);
            }
        }

        VehicleRoutingTransportCosts costMatrix = costMatrixBuilder.build();
        vrpBuilder.setRoutingCost(costMatrix);
    }

    private StateManager buildStateManager(VehicleRoutingProblem vrp) {
        return new StateManager(vrp);
    }

    private ConstraintManager buildConstraintManager(VehicleRoutingProblem vrp, StateManager stateManager) {
        return new ConstraintManager(vrp, stateManager);
    }

    private void addMaxDistanceConstraint(VehicleRoutingProblem vrp, StateManager stateManager, ConstraintManager constraintManager) {

        StateId stateId = stateManager.createStateId(DISTANCE_STATE_NAME);

        constraintManager.addConstraint((iFacts, prevAct, newAct, nextAct, prevActDepTime) -> {

            if (newAct instanceof PickupShipment) {
                stateManager.putRouteState(iFacts.getRoute(), stateId, 0.0);
            } else {
                Vehicle vehicle = iFacts.getNewVehicle();

                double distance = stateManager.getRouteState(iFacts.getRoute(), stateId, Double.class);
                double additionalDistance = vrp.getTransportCosts().getDistance(prevAct.getLocation(), newAct.getLocation(), prevActDepTime, vehicle);
                double distanceToDepot = vrp.getTransportCosts().getDistance(newAct.getLocation(), vehicle.getStartLocation(), prevActDepTime, vehicle);
                int vehicleAutonomy = vehicle.getType().getCapacityDimensions().get(AUTONOMY_IN_METER_INDEX);

                if (distance + additionalDistance + distanceToDepot > vehicleAutonomy) {
                    return HardActivityConstraint.ConstraintsStatus.NOT_FULFILLED_BREAK;
                }

                stateManager.putRouteState(iFacts.getRoute(), stateId, distance + additionalDistance);
            }

            return HardActivityConstraint.ConstraintsStatus.FULFILLED;
        }, ConstraintManager.Priority.CRITICAL);
    }

    private void organize(VehicleRoutingProblemSolution solution) {
        List<VehicleRoute> vehicleRouteList = new ArrayList<>(solution.getRoutes());

        int deliveryManIndex = 0;
        for (VehicleRoute route : vehicleRouteList) {

            DeliveryVehicle deliveryVehicle = deliveryVehicleService.findById(Integer.parseInt(route.getVehicle().getId()));
            DeliveryMan deliveryMan = deliveryManList.get(deliveryManIndex);

            Trip trip = new Trip();
            trip.setDeliveryVehicle(deliveryVehicle);
            deliveryVehicle.setTrip(trip);

            deliveryVehicle.setDeliveryMan(deliveryMan);
            deliveryMan.setDeliveryVehicle(deliveryVehicle);

            for (TourActivity act : route.getActivities()) {
                String jobId;
                if (act instanceof TourActivity.JobActivity jobActivity && jobActivity.getName().equals("deliverShipment")) {
                    jobId = jobActivity.getJob().getId();
                    double jobArriveTime = jobActivity.getArrTime();

                    Delivery delivery = deliveryService.findById(Integer.parseInt(jobId));

                    delivery.setEstimatedDeliveryTime(LocalDateTime.now().plusHours(3).plusSeconds((long) jobArriveTime));
                    delivery.setScheduled(true);

                    deliveryVehicle.setCurrentLoadKg(deliveryVehicle.getCurrentLoadKg() + delivery.getWeightKg());

                    delivery.setTrip(trip);
                    trip.getDeliveries().addLast(delivery);

                }
            }
            deliveryManIndex++;

            List<Point> wayPoints = extractWaypoints(trip.getDeliveries());
            String polylineStandard = osrmService.getNavigationData(wayPoints, NavigationType.STANDARD);
            String polylineElevation = osrmService.getNavigationData(wayPoints, NavigationType.ELEVATION_OPTIMIZED);

            trip.setPolylineStandard(polylineStandard);
            trip.setPolylineElevation(polylineElevation);
            tripService.save(trip);

            deliveryVehicleService.save(deliveryVehicle);
        }
    }


    private List<Point> extractWaypoints(List<Delivery> deliveryList) {

        return deliveryList.stream()
                .map(Delivery::getReceiverCoordinates)
                .collect(Collectors.toList());
    }

}
