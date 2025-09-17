package co.edu.uniandes.dse.parcial1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcial1.entities.RutaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.RutaRepository;
import jakarta.transaction.Transactional;

@Service
public class RutaService {

    @Autowired //Necesario para que esta variable/dependencia sea inyectada
    RutaRepository rutaRepository;

    @Transactional 
    public RutaEntity createRuta(RutaEntity foro) {
        return rutaRepository.save(foro); 
    }
}

