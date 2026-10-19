package com.emin.e_commerce.service.ListOfValues;

import com.emin.e_commerce.dto.DtoMapper;
import java.util.List;


import org.springframework.stereotype.Service;

import com.emin.e_commerce.dto.ListOfValuesDto;
import com.emin.e_commerce.exception.AlreadyExistsException;
import com.emin.e_commerce.exception.NotFoundException;
import com.emin.e_commerce.model.ListOfValues;
import com.emin.e_commerce.repository.ListOfValuesRepository;
import com.emin.e_commerce.request.ListOfValuesRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListOfValuesServiceImpl implements IListOfValuesService {
    private final DtoMapper dtoMapper;
    private final ListOfValuesRepository listOfValuesRepository;
    @Override
    @Transactional
    public ListOfValuesDto create(ListOfValuesRequest listOfValuesRequest) {
       if (listOfValuesRepository.existsByTypeAndCode(
        listOfValuesRequest.getType(),listOfValuesRequest.getCode())) {
            throw new AlreadyExistsException(listOfValuesRequest.getType() 
            + " - "+ listOfValuesRequest.getCode() + " already exists");
        }
        ListOfValues entity = new ListOfValues();
        entity.setCode(listOfValuesRequest.getCode());
        entity.setValue(listOfValuesRequest.getValue());
        entity.setType(listOfValuesRequest.getType());
        ListOfValues savedEntity = listOfValuesRepository.save(entity);
        return dtoMapper.toListOfValuesDto(savedEntity);
    }

    @Override
    public List<ListOfValuesDto> getAll() {
        return listOfValuesRepository.findAll().stream()
            .map(dtoMapper::toListOfValuesDto)
            .toList();
    }

    @Override
    public List<ListOfValuesDto> getByType(String type) {
        return listOfValuesRepository.findByType(type).stream()
                .map(dtoMapper::toListOfValuesDto)
                .toList();
    }

    @Override
    public void delete(Long id) {
       if (!listOfValuesRepository.existsById(id)) {
            throw new NotFoundException("Value not found! ID: " + id);
        }
        listOfValuesRepository.deleteById(id);
    }

    @Override
    public ListOfValues getByTypeAndCode(String type, String code) {
    ListOfValues listOfValues = listOfValuesRepository.findByTypeAndCode(type, code)
            .orElseThrow(() -> new NotFoundException(type + " - " + code + " not found"));
    return listOfValues;
        
    }

}
