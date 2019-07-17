package hieuboy.developer.services;

import hieuboy.developer.models.AjaxResultPagingModel;
import hieuboy.developer.models.ManufacturerModel;

import java.util.List;

public interface IManufacturerService {

    List<ManufacturerModel> getAllManufacturer();

    AjaxResultPagingModel getListManufacturerModel(ManufacturerModel model);

    boolean saveManufacturer(ManufacturerModel model);

    int updateManufacturer(ManufacturerModel model);

    boolean deleteManufacturer(Integer id);

    boolean checkNameManufacturer(String name);

    ManufacturerModel getDetailManufacturer(Integer id);
}
