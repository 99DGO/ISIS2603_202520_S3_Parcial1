package co.edu.uniandes.dse.parcial1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniandes.dse.parcial1.entities.EstacionEntity;
import co.edu.uniandes.dse.parcial1.entities.RutaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.repositories.EstacionRepository;
import co.edu.uniandes.dse.parcial1.repositories.RutaRepository;
import jakarta.transaction.Transactional;

@Service
public class EstacionRutaService {

    @Autowired
    private EstacionRepository estacionRepository;

    @Autowired
    private RutaRepository rutaRepository;

    @Transactional
    public EstacionEntity addEstaicon(Long rutaId, Long estacionId) throws EntityNotFoundException, IllegalOperationException{
        Optional<RutaEntity> rutaEntity = rutaRepository.findById(rutaId);
        if (rutaEntity.isEmpty())
            throw new EntityNotFoundException("Ruta no encontrado");

        Optional<EstacionEntity> estacionEntity = estacionRepository.findById(estacionId);
        if (estacionEntity.isEmpty())
            throw new EntityNotFoundException("Estaion no encontrado");

        RutaEntity rutaEntityTrue = rutaEntity.get();
        EstacionEntity estacionEntityTrue = estacionEntity.get();
       
        for (int i =0; i< rutaEntityTrue.getEstaciones().size(); i++) {

            EstacionEntity estacionTemp = rutaEntityTrue.getEstaciones().get(i);

            if (estacionTemp.getId().equals(estacionEntityTrue.getId()))
                throw new IllegalOperationException("Estacion ya está");
        }

        rutaEntityTrue.getEstaciones().add(estacionEntityTrue);

        return estacionEntityTrue;

    }


    @Transactional
    public void removeEstacionRuta(Long rutaId, Long estacionId) throws EntityNotFoundException, IllegalOperationException{
        Optional<RutaEntity> rutaEntity = rutaRepository.findById(rutaId);
        if (rutaEntity.isEmpty())
            throw new EntityNotFoundException("Ruta no encontrado");

        Optional<EstacionEntity> estacionEntity = estacionRepository.findById(estacionId);
        if (estacionEntity.isEmpty())
            throw new EntityNotFoundException("Estaion no encontrado");

        RutaEntity rutaEntityTrue = rutaEntity.get();
        EstacionEntity estacionEntityTrue = estacionEntity.get();

        Boolean guard = false;
        int indexEstacion = 0;
        for (int i =0; i< rutaEntityTrue.getEstaciones().size(); i++) {

            EstacionEntity estacionTemp = rutaEntityTrue.getEstaciones().get(i);

            if (estacionTemp.getId().equals(estacionEntityTrue.getId()))
                guard=true;
                indexEstacion=i;

        }

        int indexRuta = 0;
        for (int i =0; i< estacionEntityTrue.getRutas().size(); i++) {

            RutaEntity rutaTemp = estacionEntityTrue.getRutas().get(i);

            if (rutaTemp.getId().equals(rutaEntityTrue.getId()))
                indexRuta=i;
        }

        if (guard) {
            rutaEntityTrue.getEstaciones().remove(indexEstacion);
            estacionEntityTrue.getRutas().remove(indexRuta);
        }
        else{
            throw new IllegalOperationException("Estacion no hace parte de la ruta");
        }
    }
}

