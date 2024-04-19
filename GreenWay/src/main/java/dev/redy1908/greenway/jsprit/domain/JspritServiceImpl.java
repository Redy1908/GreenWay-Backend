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
import dev.redy1908.greenway.jsprit.domain.exceptions.models.NoDeliveryToOrganizeException;
import dev.redy1908.greenway.osrm.domain.IOsrmService;
import dev.redy1908.greenway.vehicle_deposit.domain.IVehicleDepositService;
import dev.redy1908.greenway.vehicle_deposit.domain.VehicleDeposit;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class JspritServiceImpl implements IJspritService {

    private final IOsrmService osrmService;
    private final IDeliveryVehicleService deliveryVehicleService;
    private final IDeliveryService deliveryService;
    private final IDeliveryManService deliveryManService;
    private final IVehicleDepositService depositService;

    private List<Delivery> deliveryList;
    private List<DeliveryVehicle> vehicleList;
    private List<DeliveryMan> deliveryManList;
    private Pair<double[][], double[][]> matrices;

    private static final int AUTONOMY_IN_METER_INDEX = 0;
    private static final int WEIGHT_IN_KG_INDEX = 1;

    private static final int EST_CLIENT_RETRIEVE_TIME_SECONDS = 420;

    @Scheduled(cron = "0 0 6 * * ?")
    public void schedule() {

        loadData();

        double[][] matrixDurations = matrices.getFirst();
        double[][] matrixDistances = matrices.getSecond();

        System.out.println(matrixDistances[0][1]);

        int squareMatrixSize = deliveryList.size() + 1;

        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
        vrpBuilder.setFleetSize(VehicleRoutingProblem.FleetSize.FINITE);

        addVehicles(vrpBuilder, deliveryManList, vehicleList);
        addDeliveries(vrpBuilder, deliveryList);
        setRoutingCosts(vrpBuilder, squareMatrixSize, squareMatrixSize, matrixDistances, matrixDurations);

        VehicleRoutingProblem vrp = vrpBuilder.build();

        StateManager stateManager = buildStateManager(vrp);
        ConstraintManager constraintManager = buildConstraintManager(vrp, stateManager);
        addMaxDistanceConstraint(vrp, stateManager, constraintManager);

        VehicleRoutingAlgorithm vra = Jsprit.Builder.newInstance(vrp).setStateAndConstraintManager(stateManager, constraintManager).buildAlgorithm();

        Collection<VehicleRoutingProblemSolution> solutions = vra.searchSolutions();

        VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);

        SolutionPrinter.print(vrp, bestSolution, SolutionPrinter.Print.VERBOSE);

        organize(bestSolution);
    }

    private void loadData() {
        deliveryList = deliveryService.findUnassignedDeliveries();
        vehicleList = deliveryVehicleService.findAll();
        deliveryManList = deliveryManService.findAll();
        VehicleDeposit vehicleDeposit = depositService.getVehicleDeposit();

        if (deliveryList.isEmpty()) {
            throw new NoDeliveryToOrganizeException();
        }

        matrices = osrmService.getMatrixDistances(vehicleDeposit, deliveryList);
    }

    private void addVehicles(VehicleRoutingProblem.Builder vrpBuilder, List<DeliveryMan> deliveryManList, List<DeliveryVehicle> deliveryVehicleList) {

        for (int i = 0; i < deliveryManList.size(); i++) {

            DeliveryMan deliveryMan = deliveryManList.get(i);

            if (i < deliveryVehicleList.size()) {
                DeliveryVehicle deliveryVehicle = deliveryVehicleList.get(i);
                String id = deliveryVehicle.getId().toString();

                VehicleType type = VehicleTypeImpl.Builder.newInstance(id)
                        .addCapacityDimension(AUTONOMY_IN_METER_INDEX, deliveryVehicle.getMaxAutonomyKm() * 1000)
                        .addCapacityDimension(WEIGHT_IN_KG_INDEX, deliveryVehicle.getMaxCapacityKg() - deliveryVehicle.getCurrentLoadKg())
                        .build();

                VehicleImpl vehicle = VehicleImpl.Builder.newInstance(id)
                        .setStartLocation(Location.newInstance("0"))
                        .setReturnToDepot(true)
                        .setType(type)
                        .build();

                vrpBuilder.addVehicle(vehicle);

                deliveryMan.setDeliveryVehicle(deliveryVehicle);
                deliveryVehicle.setDeliveryMan(deliveryMan);
                deliveryVehicleService.save(deliveryVehicle);
            }
        }
    }

    private void addDeliveries(VehicleRoutingProblem.Builder vrpBuilder, List<Delivery> deliveryList) {

        int pos = 1;
        for (Delivery delivery : deliveryList) {
            String id = delivery.getId().toString();

            Shipment service = Shipment.Builder.newInstance(id)
                    .setPickupLocation(Location.newInstance("0"))
                    .setDeliveryLocation(Location.newInstance(Integer.toString(pos)))
                    .setDeliveryServiceTime(EST_CLIENT_RETRIEVE_TIME_SECONDS)
                    .addSizeDimension(WEIGHT_IN_KG_INDEX, delivery.getWeightKg())
                    .build();

            vrpBuilder.addJob(service);
            pos++;
        }
    }

    private void setRoutingCosts(VehicleRoutingProblem.Builder vrpBuilder, int i, int j, double[][] matrixDistances, double[][] matrixDurations) {

        VehicleRoutingTransportCostsMatrix.Builder costMatrixBuilder = VehicleRoutingTransportCostsMatrix.Builder.newInstance(false);

        for (int from = 0; from < i; from++) {
            for (int to = 0; to < j; to++) {
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

        StateId stateId = stateManager.createStateId("distance");

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

        for (VehicleRoute route : vehicleRouteList) {

            DeliveryVehicle deliveryVehicle = deliveryVehicleService.findById(Integer.parseInt(route.getVehicle().getId()));

            for (TourActivity act : route.getActivities()) {
                String jobId;
                if (act instanceof TourActivity.JobActivity jobActivity) {
                    jobId = jobActivity.getJob().getId();
                    double jobArriveTime = jobActivity.getArrTime();

                    Delivery delivery = deliveryService.findById(Integer.parseInt(jobId));
                    delivery.setEstimatedDeliveryTime(LocalDateTime.now().plusHours(2).plusSeconds((long) jobArriveTime));
                    delivery.setDeliveryVehicle(deliveryVehicle);

                    deliveryVehicle.getDeliveries().addLast(delivery);
                    deliveryVehicle.setCurrentLoadKg(deliveryVehicle.getCurrentLoadKg() + delivery.getWeightKg());
                    deliveryVehicleService.save(deliveryVehicle);
                }
            }
        }
    }

}
