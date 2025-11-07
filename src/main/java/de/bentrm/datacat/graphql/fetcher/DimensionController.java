package de.bentrm.datacat.graphql.fetcher;

import de.bentrm.datacat.catalog.domain.XtdDimension;
import de.bentrm.datacat.catalog.domain.XtdRational;
import de.bentrm.datacat.catalog.service.DimensionRecordService;
import de.bentrm.datacat.catalog.specification.CatalogRecordSpecification;
import de.bentrm.datacat.graphql.Connection;
import de.bentrm.datacat.graphql.dto.FilterInput;
import de.bentrm.datacat.graphql.dto.SpecificationMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class DimensionController {

    @Autowired
    private DimensionRecordService dimensionRecordService;

    @Autowired
    private SpecificationMapper specificationMapper;

    @QueryMapping
    public Optional<XtdDimension> getDimension(@Argument String id) {
        return dimensionRecordService.findByIdWithDirectRelations(id, XtdDimension.class.getSimpleName());
    }

    @QueryMapping
    public Connection<XtdDimension> findDimensions (@Argument FilterInput input) {
        if (input == null) input = new FilterInput();
        final CatalogRecordSpecification specification = specificationMapper.toCatalogRecordSpecification(input);
        final Page<XtdDimension> page = dimensionRecordService.findAll(specification);
        return Connection.of(page);
    }

    @BatchMapping(typeName = "XtdDimension", field = "thermodynamicTemperatureExponent")
    public Map<XtdDimension, Optional<XtdRational>> getThermodynamicTemperatureExponent(List<XtdDimension> dimensions) {
        return dimensions.stream()
                .filter(dimension -> dimension != null)  // Filter out null dimensions
                .collect(Collectors.toMap(
                        dimension -> dimension,
                        dimension -> {
                            Optional<XtdRational> result = dimensionRecordService.getThermodynamicTemperatureExponent(dimension);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdDimension", field = "electricCurrentExponent")
    public Map<XtdDimension, Optional<XtdRational>> getElectricCurrentExponent(List<XtdDimension> dimensions) {
        return dimensions.stream()
                .filter(dimension -> dimension != null)  // Filter out null dimensions
                .collect(Collectors.toMap(
                        dimension -> dimension,
                        dimension -> {
                            Optional<XtdRational> result = dimensionRecordService.getElectricCurrentExponent(dimension);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdDimension", field = "lengthExponent")
    public Map<XtdDimension, Optional<XtdRational>> getLengthExponent(List<XtdDimension> dimensions) {
        return dimensions.stream()
                .filter(dimension -> dimension != null)  // Filter out null dimensions
                .collect(Collectors.toMap(
                        dimension -> dimension,
                        dimension -> {
                            Optional<XtdRational> result = dimensionRecordService.getLengthExponent(dimension);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdDimension", field = "luminousIntensityExponent")
    public Map<XtdDimension, Optional<XtdRational>> getLuminousIntensityExponent(List<XtdDimension> dimensions) {
        return dimensions.stream()
                .filter(dimension -> dimension != null)  // Filter out null dimensions
                .collect(Collectors.toMap(
                        dimension -> dimension,
                        dimension -> {
                            Optional<XtdRational> result = dimensionRecordService.getLuminousIntensityExponent(dimension);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdDimension", field = "amountOfSubstanceExponent")
    public Map<XtdDimension, Optional<XtdRational>> getAmountOfSubstanceExponent(List<XtdDimension> dimensions) {
        return dimensions.stream()
                .filter(dimension -> dimension != null)  // Filter out null dimensions
                .collect(Collectors.toMap(
                        dimension -> dimension,
                        dimension -> {
                            Optional<XtdRational> result = dimensionRecordService.getAmountOfSubstanceExponent(dimension);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdDimension", field = "massExponent")
    public Map<XtdDimension, Optional<XtdRational>> getMassExponent(List<XtdDimension> dimensions) {
        return dimensions.stream()
                .filter(dimension -> dimension != null)  // Filter out null dimensions
                .collect(Collectors.toMap(
                        dimension -> dimension,
                        dimension -> {
                            Optional<XtdRational> result = dimensionRecordService.getMassExponent(dimension);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }

    @BatchMapping(typeName = "XtdDimension", field = "timeExponent")
    public Map<XtdDimension, Optional<XtdRational>> getTimeExponent(List<XtdDimension> dimensions) {
        return dimensions.stream()
                .filter(dimension -> dimension != null)  // Filter out null dimensions
                .collect(Collectors.toMap(
                        dimension -> dimension,
                        dimension -> {
                            Optional<XtdRational> result = dimensionRecordService.getTimeExponent(dimension);
                            return result != null ? result : Optional.empty();  // Handle null Optional
                        }
                ));                
    }
}
