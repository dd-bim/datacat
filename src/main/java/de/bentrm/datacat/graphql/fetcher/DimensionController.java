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
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

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

    @SchemaMapping(typeName = "XtdDimension", field = "thermodynamicTemperatureExponent")
    public Optional<XtdRational> getThermodynamicTemperatureExponent(XtdDimension dimension) {
        return dimensionRecordService.getThermodynamicTemperatureExponent(dimension);
    }

    @SchemaMapping(typeName = "XtdDimension", field = "electricCurrentExponent")
    public Optional<XtdRational> getElectricCurrentExponent(XtdDimension dimension) {
        return dimensionRecordService.getElectricCurrentExponent(dimension);
    }

    @SchemaMapping(typeName = "XtdDimension", field = "lengthExponent")
    public Optional<XtdRational> getLengthExponent(XtdDimension dimension) {
        return dimensionRecordService.getLengthExponent(dimension);
    }

    @SchemaMapping(typeName = "XtdDimension", field = "luminousIntensityExponent")
    public Optional<XtdRational> getLuminousIntensityExponent(XtdDimension dimension) {
        return dimensionRecordService.getLuminousIntensityExponent(dimension);
    }

    @SchemaMapping(typeName = "XtdDimension", field = "amountOfSubstanceExponent")
    public Optional<XtdRational> getAmountOfSubstanceExponent(XtdDimension dimension) {
        return dimensionRecordService.getAmountOfSubstanceExponent(dimension);
    }

    @SchemaMapping(typeName = "XtdDimension", field = "massExponent")
    public Optional<XtdRational> getMassExponent(XtdDimension dimension) {
        return dimensionRecordService.getMassExponent(dimension);
    }

    @SchemaMapping(typeName = "XtdDimension", field = "timeExponent")
    public Optional<XtdRational> getTimeExponent(XtdDimension dimension) {
        return dimensionRecordService.getTimeExponent(dimension);
    }
}
