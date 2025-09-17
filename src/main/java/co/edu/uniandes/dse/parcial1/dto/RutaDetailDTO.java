package co.edu.uniandes.dse.parcial1.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class RutaDetailDTO extends RutaDTO {
	private List<EstacionDTO> estaciones = new ArrayList<>();
}