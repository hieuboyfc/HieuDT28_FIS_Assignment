package hieuboy.developer.services.impls;

import hieuboy.developer.entities.Manufacturer;
import hieuboy.developer.entities.User;
import hieuboy.developer.mappers.ManufacturerMapper;
import hieuboy.developer.models.AjaxResultPagingModel;
import hieuboy.developer.models.ManufacturerModel;
import hieuboy.developer.repositories.ManufacturerRepository;
import hieuboy.developer.repositories.UserRepository;
import hieuboy.developer.services.IManufacturerService;
import hieuboy.developer.utils.AuthenticationHelper;
import hieuboy.developer.utils.HelperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;

@Service
public class ManufacturerServiceImpl implements IManufacturerService {

    private static final Logger log4j = LoggerFactory.getLogger(ManufacturerServiceImpl.class);

    private ManufacturerRepository manufacturerRepository;

    private UserRepository userRepository;

    public ManufacturerServiceImpl(ManufacturerRepository manufacturerRepository, UserRepository userRepository) {
        this.manufacturerRepository = manufacturerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ManufacturerModel> getAllManufacturer() {
        return ManufacturerMapper.MAPPER.mapListEntityToListModel(manufacturerRepository.findAll().stream());
    }

    @Override
    public AjaxResultPagingModel getListManufacturerModel(ManufacturerModel model) {
        Sort sort = new Sort(model.getDesending() == 1 ? Sort.Direction.DESC : Sort.Direction.ASC, model.getColumn());
        Pageable pageable = PageRequest.of(model.getPageIndex(), model.getPageSize(), sort);
        Page<Manufacturer> lstManufacturer =
                manufacturerRepository.getListManufacturerAndSearch(model.getName(), pageable);
        return new HelperService().pagingModel(model.getColumn(), model.getDesending(), model.getPageIndex(),
                model.getPageSize(), lstManufacturer.getTotalPages(), lstManufacturer.getTotalElements(),
                ManufacturerMapper.MAPPER.mapListEntityToListModel(lstManufacturer.getContent().stream()));
    }

    @Override
    @Transactional
    public boolean saveManufacturer(ManufacturerModel model) {
        try {
            Calendar calendar = Calendar.getInstance();
            Manufacturer manufacturer = ManufacturerMapper.MAPPER.mapModelToEntity(model);
            saveData(manufacturer, calendar);
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public int updateManufacturer(ManufacturerModel model) {
        try {
            Manufacturer manufacturer = manufacturerRepository.findByManufacturerId(model.getId());
            if (manufacturer != null) {
                ManufacturerMapper.MAPPER.updateFromDTOToEntity(model, manufacturer);
                manufacturerRepository.save(manufacturer);
                return 1;
            }
            return 2;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return 0;
        }
    }

    @Override
    @Transactional
    public boolean deleteManufacturer(Integer id) {
        try {
            manufacturerRepository.deleteByManufacturerId(id);
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean checkNameManufacturer(String name) {
        return manufacturerRepository.findByName(name) == null;
    }

    @Override
    public ManufacturerModel getDetailManufacturer(Integer id) {
        try {
            Manufacturer manufacturer = manufacturerRepository.findByManufacturerId(id);
            if (manufacturer != null)
                return ManufacturerMapper.MAPPER.mapEntityToModel(manufacturer);
        } catch (Exception e) {
            log4j.error(e.getMessage());
        }
        return null;
    }

    private void saveData(Manufacturer manufacturer, Calendar calendar) {
        User userLogin = new AuthenticationHelper(userRepository).getUsernameLoginInfo();
        manufacturer.setUsercreated(userLogin.getUsername());
        manufacturer.setDatecreated(calendar.getTime());
        manufacturerRepository.save(manufacturer);
    }

}
