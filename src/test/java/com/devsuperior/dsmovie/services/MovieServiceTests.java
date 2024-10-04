package com.devsuperior.dsmovie.services;

import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {

	@InjectMocks
	private MovieService service;

	@Mock
	private MovieRepository repository;

	private Long existingId;
	private Long nonExistingId;
	private MovieEntity movie;
	private MovieDTO movieDTO;
	private PageImpl<MovieEntity> page;

	@BeforeEach
	void setUp() throws Exception {
		
		existingId = 1L;
		nonExistingId = 2L;		

		movie = MovieFactory.createMovieEntity();
		movieDTO = MovieFactory.createMovieDTO();
		page = new PageImpl<>(List.of(movie));

		Mockito.when(repository.searchByTitle(any(), (Pageable) ArgumentMatchers.any())).thenReturn(page);

		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(movie));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
	}

	@Test
	public void findAllShouldReturnPagedMovieDTO() {
		Pageable pageable = PageRequest.of(0, 10);

		Page<MovieDTO> result = service.findAll("", pageable);

		Assertions.assertNotNull(result);
		Mockito.verify(repository).searchByTitle("", pageable);
	}

	@Test
	public void findByIdShouldReturnMovieDTOWhenIdExists() {
		MovieDTO result = service.findById(existingId);

		Assertions.assertNotNull(result);
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
	}

	@Test
	public void insertShouldReturnMovieDTO() {
	}

	@Test
	public void updateShouldReturnMovieDTOWhenIdExists() {
	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
	}

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
	}
}
