package hieuboy.developer.restcontrollers;

import hieuboy.developer.models.AjaxResult;
import hieuboy.developer.models.AjaxResultModel;
import hieuboy.developer.models.AjaxResultPagingModel;
import hieuboy.developer.models.ManufacturerModel;
import hieuboy.developer.services.IManufacturerService;
import hieuboy.developer.services.IManufacturerService;
import hieuboy.developer.utils.Value;
import hieuboy.developer.utils.ValueAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ManufacturerRestController {

    private static final Logger log4j = LoggerFactory.getLogger(ManufacturerRestController.class);

    private IManufacturerService manufacturerService;

    public ManufacturerRestController(IManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }

    @GetMapping(value = ValueAPI.MANUFACTURER_ADMIN, produces = "application/json")
    public ModelAndView getListManufacturer() {
        ModelAndView mav = new ModelAndView("admin/views/front_end/manufacturer/manufacturer");
        try {
            ManufacturerModel model = new ManufacturerModel();
            model.setPageIndex(0);
            model.setPageSize(Value.PAGE_SIZE);
            model.setDesending(1);
            model.setColumn("id");
            AjaxResultPagingModel pagingModel = manufacturerService.getListManufacturerModel(model);
            mav.addObject("active", "manufacturer");
            mav.addObject("lstManufacturer", pagingModel);
            return mav;
        } catch (Exception e) {
            log4j.error("CAN NOT GET DATA: " + e.getMessage());
            return mav;
        }
    }

    @GetMapping(value = ValueAPI.MANUFACTURER_API + ValueAPI.LOAD_DATA, produces = "application/json")
    public ModelAndView loadDataManufacturer(@ModelAttribute ManufacturerModel manufacturerModel) {
        ModelAndView mav = new ModelAndView("admin/views/front_end/manufacturer/manufacturer_load_data");
        try {
            if (manufacturerModel.getColumn().equals("")) manufacturerModel.setColumn("id");
            manufacturerModel.setDesending(manufacturerModel.getDesending());
            manufacturerModel.setPageSize(manufacturerModel.getPageSize());
            manufacturerModel.setPageIndex(manufacturerModel.getPageIndex() - 1);
            AjaxResultPagingModel pagingModel = manufacturerService.getListManufacturerModel(manufacturerModel);
            mav.addObject("lstManufacturer", pagingModel);
            return mav;
        } catch (Exception e) {
            log4j.error("CAN NOT GET DATA: " + e.getMessage());
            return mav;
        }
    }

    @GetMapping(value = ValueAPI.MANUFACTURER_API + ValueAPI.GET_DETAIL_DATA, produces = "application/json")
    public AjaxResultModel<ManufacturerModel> getDetailManufacturer(@PathVariable Integer id) {
        AjaxResultModel<ManufacturerModel> result = new AjaxResultModel<>();
        try {
            ManufacturerModel manufacturerModel = manufacturerService.getDetailManufacturer(id);
            result.setResult(true);
            result.setResultData(manufacturerModel);
            return result;
        } catch (Exception e) {
            log4j.error("CAN NOT FIND ID MANUFACTURER: " + e.getMessage());
            result.setResult(false);
            return result;
        }
    }

    @PostMapping(value = ValueAPI.MANUFACTURER_API + ValueAPI.SAVE_DATA, produces = "application/json")
    public AjaxResult saveDataManufacturer(@ModelAttribute ManufacturerModel manufacturerModel) {
        AjaxResult result = new AjaxResult();
        try {
            boolean check = manufacturerService.saveManufacturer(manufacturerModel);
            result.setResult(check);
            return result;
        } catch (Exception e) {
            log4j.error("CAN NOT SAVE DATA MANUFACTURER: " + e.getMessage());
            return result;
        }
    }

    @PutMapping(value = ValueAPI.MANUFACTURER_API + ValueAPI.UPDATE_DATA, produces = "application/json")
    public AjaxResult updateManufacturer(@ModelAttribute ManufacturerModel manufacturerModel) {
        AjaxResult result = new AjaxResult();
        try {
            int code = manufacturerService.updateManufacturer(manufacturerModel);
            result.setCode(code);
            result.setResult(true);
            return result;
        } catch (Exception e) {
            log4j.error("CAN NOT UPDATE MANUFACTURER: " + e.getMessage());
            return result;
        }
    }

    @DeleteMapping(value = ValueAPI.MANUFACTURER_API + ValueAPI.DELETE_DATA, produces = "application/json")
    public boolean deleteDataManufacturer(@PathVariable Integer id) {
        try {
            return manufacturerService.deleteManufacturer(id);
        } catch (Exception e) {
            log4j.error("CAN NOT DELETE DATA MANUFACTURER: " + e.getMessage());
            return false;
        }
    }

    @PostMapping(value = ValueAPI.MANUFACTURER_API_CHECK_NAME, produces = "application/json")
    public boolean checkNameManufacturer(String name) {
        try {
            return manufacturerService.checkNameManufacturer(name);
        } catch (Exception e) {
            log4j.error("CAN NOT FIND NAME: " + e.getMessage());
            return false;
        }
    }

}
