package com.devsuperior.dsmovie.services;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {

	@InjectMocks
	private ScoreService service;

	@Mock
	private ScoreRepository repository;

	@Mock
	private UserService userService;

	@Mock
	private MovieRepository movieRepository;

	private long existingId;
	private long nonExistingId;
	private UserEntity authUser;
	private MovieEntity movie;
	private ScoreEntity score;
	private ScoreDTO scoreDTO;

	@BeforeEach
	void setUp() throws Exception {

		existingId = 1L;
		nonExistingId = 2L;
		authUser = UserFactory.createUserEntity();
		movie = MovieFactory.createMovieEntity();
		score = ScoreFactory.createScoreEntity();
		movie.getScores().add(score);
		scoreDTO = ScoreFactory.createScoreDTO();		
		
		Mockito.when(userService.authenticated()).thenReturn(authUser);
		
		Mockito.when(movieRepository.findById(existingId)).thenReturn(Optional.of(movie));
		Mockito.when(movieRepository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.when(repository.saveAndFlush(ArgumentMatchers.any())).thenReturn(score);
		
		Mockito.when(movieRepository.save(ArgumentMatchers.any())).thenReturn(movie);
	}

	@Test
	public void saveScoreShouldReturnMovieDTO() {
		
		MovieDTO result = service.saveScore(scoreDTO);

		Assertions.assertNotNull(result);
	}

	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
		movie.setId(nonExistingId);
		score.setMovie(movie);
		
		ScoreDTO scoreDTOWithNonExistingMovieId = new ScoreDTO(score);
		
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.saveScore(scoreDTOWithNonExistingMovieId);
		});
	}
}
