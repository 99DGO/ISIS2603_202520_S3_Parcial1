package co.edu.uniandes.dse.parcial1.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.parcial1.entities.EstacionEntity;
import co.edu.uniandes.dse.parcial1.entities.RutaEntity;
import co.edu.uniandes.dse.parcial1.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcial1.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcial1.services.EstacionRutaService;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@Import(EstacionRutaService.class)
class EstacionRutaServiceTest {

	private PodamFactory factory = new PodamFactoryImpl();

	@Autowired
	private EstacionRutaService estacionRutaService;

	@Autowired
	private TestEntityManager entityManager;

	private List<RutaEntity> rutasList = new ArrayList<>();
	private List<EstacionEntity> estacionList = new ArrayList<>();

	/**
	 * Configuración inicial de la prueba.
	 */
	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	/**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from EstacionEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from RutaEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		for (int i = 0; i < 3; i++) {
			RutaEntity rutaEntity = factory.manufacturePojo(RutaEntity.class);
			EstacionEntity estacionEntity = factory.manufacturePojo(EstacionEntity.class);

			estacionEntity.getRutas().add(rutaEntity);
			rutaEntity.getEstaciones().add(estacionEntity);

			entityManager.persist(estacionEntity);
			entityManager.persist(rutaEntity);
			estacionList.add(estacionEntity);
			rutasList.add(rutaEntity);
		}

		RutaEntity rutaEntity = factory.manufacturePojo(RutaEntity.class);

		entityManager.persist(rutaEntity);
		rutasList.add(rutaEntity);

		EstacionEntity estacionEntity = factory.manufacturePojo(EstacionEntity.class);
		entityManager.persist(estacionEntity);
		estacionList.add(estacionEntity);
	}

	/*
	 * Asegúrese de crear
	cuatro pruebas: (1) la ruta se agrega correctamente; (2) se lanza una excepción de negocio
	porque la estación no existe; (3) se lanza una excepción de negocio porque la ruta no
	existe; y (4) se lanza una excepción de negocio porque la estación ya tenía la ruta
	 */

	@Test
	void testAddRuta() throws EntityNotFoundException, IllegalOperationException {
		RutaEntity entity = rutasList.get(0);
		EstacionEntity estacionEntity = estacionList.get(2);
		RutaEntity response = estacionRutaService.addRuta(entity.getId(), estacionEntity.getId());

		assertNotNull(response);
		assertEquals(entity.getId(), response.getId());
	}

	@Test
	void testAddInvalidRuta() throws EntityNotFoundException, IllegalOperationException{
		assertThrows(EntityNotFoundException.class, () -> {
			EstacionEntity estacionEntity = estacionList.get(1);
			estacionRutaService.addRuta(0L, estacionEntity.getId());
		});
	}

	@Test
	void testAddRutaInvalidEstacion() throws EntityNotFoundException, IllegalOperationException{
		assertThrows(EntityNotFoundException.class, () -> {
			RutaEntity entity = rutasList.get(0);
			estacionRutaService.addRuta(entity.getId(), 0L);
		});
	}


	@Test
	void testAddRutaEstacionRepetida() throws EntityNotFoundException, IllegalOperationException{
		assertThrows(IllegalOperationException.class, () -> {
			RutaEntity entity = rutasList.get(0);
			EstacionEntity estacionEntity = estacionList.get(1);
			RutaEntity response = estacionRutaService.addRuta(entity.getId(), estacionEntity.getId());
			RutaEntity response2 = estacionRutaService.addRuta(entity.getId(), estacionEntity.getId());
		});
	}
	/*
	 * Asegúrese de crear
		cuatro pruebas: (1) la ruta se elimina correctamente; (2) se lanza una excepción de
		negocio porque la estación no existe; (3) se lanza una excepción de negocio porque la ruta
		no existe; y (4) se lanza una excepción de negocio porque la estación dada no es parte de
		la ruta dada.
	 */

	
	@Test
	void testRemoveRuta() throws EntityNotFoundException, IllegalOperationException {
		Long idRuta = rutasList.get(0).getId();
		estacionRutaService.removeEstacionRuta(idRuta, estacionList.get(0).getId());
		EstacionEntity estacion = entityManager.find(EstacionEntity.class, estacionList.get(0).getId());
		
        for (int i =0; i< estacion.getRutas().size(); i++) {

            RutaEntity rutaTemp = estacion.getRutas().get(i);

            assertFalse(rutaTemp.getId().equals(idRuta));
        }
	}

	@Test
	void testRemoveRutaInvalidEstacion() {
		assertThrows(EntityNotFoundException.class, () -> {
			estacionRutaService.removeEstacionRuta(rutasList.get(0).getId(), 0L );
		});
	}
	
	@Test
	void testRemoveInvalidRuta() {
		assertThrows(EntityNotFoundException.class, () -> {
			estacionRutaService.removeEstacionRuta(0L, estacionList.get(0).getId() );
		});
	}

	@Test
	void testRemoveInvalidRutaEstacionNoAsociada() {
		assertThrows(IllegalOperationException.class, () -> {
			estacionRutaService.removeEstacionRuta(rutasList.get(0).getId(), estacionList.get(2).getId() );
		});
	}

}