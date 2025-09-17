package co.edu.uniandes.dse.parcial1.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.parcial1.dto.RutaDTO;
import co.edu.uniandes.dse.parcial1.entities.RutaEntity;
import co.edu.uniandes.dse.parcial1.services.RutaService;



@RestController
@RequestMapping("/rutas") //Este es el nombre que tendra que poner el usuario
public class RutaController {

    @Autowired
	private RutaService rutaService;

	@Autowired
	private ModelMapper modelMapper; 

    @PostMapping()
    @ResponseStatus(code=HttpStatus.CREATED)
    public RutaDTO create(@RequestBody RutaDTO rutaDTO) {
		RutaEntity rutaEntity = rutaService.createRuta(modelMapper.map(rutaDTO, RutaEntity.class));
		return modelMapper.map(rutaEntity, RutaDTO.class);
    }
    
    
}
