package com.example.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.dao.PresentationDao;
import com.example.entities.Presentation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PresentationServiceImpl implements PresentationService {

    private final PresentationDao presentationDao;

    @Override
    public List<Presentation> findAll() {
        // TODO Auto-generated method stub
        return presentationDao.findAll();
    }

    @Override
    public void save(Presentation presentation) {
        // TODO Auto-generated method stub
        presentationDao.save(presentation);
    }

    @Override
    public Presentation findById(int id) {
        // TODO Auto-generated method stub
        return presentationDao.findById(id).orElseThrow(()
             -> new RuntimeException("No ha sido encontrada la presentación para el id suministrado."));
    }

}
