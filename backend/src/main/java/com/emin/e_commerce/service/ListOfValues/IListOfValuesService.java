package com.emin.e_commerce.service.ListOfValues;

import java.util.List;


import com.emin.e_commerce.dto.ListOfValuesDto;
import com.emin.e_commerce.model.ListOfValues;
import com.emin.e_commerce.request.ListOfValuesRequest;

public interface IListOfValuesService {
    ListOfValuesDto create(ListOfValuesRequest listOfValuesRequest);
    
    List<ListOfValuesDto> getAll();

    List<ListOfValuesDto> getByType(String type);

    void delete(Long id);

   ListOfValues getByTypeAndCode(String type, String code);
}
