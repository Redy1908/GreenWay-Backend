package dev.redy1908.greenway.jspirit;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.cost.VehicleRoutingTransportCosts;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.core.util.VehicleRoutingTransportCostsMatrix;
import dev.redy1908.greenway.delivery.domain.Delivery;
import dev.redy1908.greenway.delivery.domain.IDeliveryService;
import dev.redy1908.greenway.delivery_vehicle.domain.DeliveryVehicle;
import dev.redy1908.greenway.delivery_vehicle.domain.IDeliveryVehicleService;
import dev.redy1908.greenway.osrm.domain.IOsrmService;
import kotlin.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JspritService {

    private final IOsrmService osrmService;
    private final IDeliveryVehicleService deliveryVehicleService;
    private final IDeliveryService deliveryService;

    private static final int WEIGHT_INDEX = 0;

    public void test(List<DeliveryVehicle> vehicleList, List<Delivery> deliveryList) {

        Pair<double[][], double[][]> matrices = osrmService.getMatrixDistances(vehicleList, deliveryList);

        double[][] matrixDurations = matrices.getFirst();
        double[][] matrixDistances = matrices.getSecond();

        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
        vrpBuilder.setFleetSize(VehicleRoutingProblem.FleetSize.INFINITE);
        
        addVehicles(vrpBuilder, vehicleList);
        addDeliveries(vrpBuilder, deliveryList);
        setRoutingCosts(vrpBuilder, vehicleList.size(), deliveryList.size(), matrixDistances, matrixDurations);
       
        VehicleRoutingProblem vrp = vrpBuilder.build();

        VehicleRoutingAlgorithm vra = Jsprit.createAlgorithm(vrp);

        Collection<VehicleRoutingProblemSolution> solutions = vra.searchSolutions();

        VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);
        SolutionPrinter.print(vrp, bestSolution, SolutionPrinter.Print.VERBOSE);

        organize(bestSolution);
    }
    
    private void addVehicles(VehicleRoutingProblem.Builder vrpBuilder, List<DeliveryVehicle> deliveryVehicleList){

        for (DeliveryVehicle deliveryVehicle : deliveryVehicleList) {
            String id = deliveryVehicle.getId().toString();

            VehicleType type = VehicleTypeImpl.Builder.newInstance(id)
                    .addCapacityDimension(WEIGHT_INDEX, deliveryVehicle.getMaxCapacityKg())
                    .build();

            VehicleImpl vehicle = VehicleImpl.Builder.newInstance(id)
                    .setStartLocation(Location.newInstance("0"))
                    .setType(type)
                    .setReturnToDepot(true)
                    .build();

            vrpBuilder.addVehicle(vehicle);
        }
    }

    private void addDeliveries(VehicleRoutingProblem.Builder vrpBuilder, List<Delivery> deliveryList){

        int pos = 1;
        for (Delivery delivery : deliveryList) {
            String id = delivery.getId().toString();

            Service service = Service.Builder.newInstance(id)
                    .setLocation(Location.newInstance(Integer.toString(pos)))
                    .addSizeDimension(WEIGHT_INDEX,delivery.getWeightKg())
                    .build();

            vrpBuilder.addJob(service);
            pos++;
        }
    }
    
    private void setRoutingCosts(VehicleRoutingProblem.Builder vrpBuilder, int i, int j, double[][] matrixDistances, double[][] matrixDurations){

        VehicleRoutingTransportCostsMatrix.Builder costMatrixBuilder = VehicleRoutingTransportCostsMatrix.Builder.newInstance(true);

        for (int from = 0; from < i; from++) {
            for (int to = 0; to < j; to++) {
                costMatrixBuilder.addTransportDistance(Integer.toString(from), Integer.toString(to), matrixDistances[from][to]);
                costMatrixBuilder.addTransportTime(Integer.toString(from), Integer.toString(to), matrixDurations[from][to]);
            }
        }

        VehicleRoutingTransportCosts costMatrix = costMatrixBuilder.build();
        vrpBuilder.setRoutingCost(costMatrix);
    }

    private void organize(VehicleRoutingProblemSolution solution){
        List<VehicleRoute> vehicleRouteList = new ArrayList<>(solution.getRoutes());

        for (VehicleRoute route : vehicleRouteList) {

            DeliveryVehicle deliveryVehicle = deliveryVehicleService.findById(Integer.parseInt(route.getVehicle().getId()));

            for (TourActivity act : route.getActivities()) {
                String jobId;
                if (act instanceof TourActivity.JobActivity jobActivity) {
                    jobId = jobActivity.getJob().getId();
                } else {
                    jobId = "-";
                }

                if(!jobId.equals("-")){
                    Delivery delivery = deliveryService.findById(Integer.parseInt(jobId));
                    deliveryVehicle.getDeliveries().add(delivery);
                    deliveryVehicleService.save(deliveryVehicle);
                }
            }
        }
    }

}
