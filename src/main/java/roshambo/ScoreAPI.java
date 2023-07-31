package roshambo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@Transactional
public class ScoreAPI {

	@PersistenceContext
	private EntityManager entityManager;
	
	@GetMapping("/thescore")
	public Score getTheMainScore() {
		Score score = entityManager.find(Score.class, 1);
		return score;
	}
	
	@GetMapping("/deletescore")
	public Score deleteTheScore() {
		Score score = entityManager.find(Score.class, 1);
		entityManager.remove(score);
		return score;
	}
	
	@GetMapping("/increasethewins")
	public Score increaseTheWins() {
		Score score = entityManager.find(Score.class, 1);
		score.setWins(score.getWins()+1);
		return score;
	}
	
	@GetMapping("/allscores")
	public List<Score> getAllScores() {
		List<Score> allScores = entityManager.createQuery("from Score", Score.class).getResultList(); //The Score in "from Score" comes from Score.java
		return allScores;
	}
	
	@GetMapping("/randomizescore")
	public Score updateScore() {
		Score score = entityManager.find(Score.class, 1);

		score.setWins((int)(Math.random()*100));
		score.setLosses((int)(Math.random()*100));
		score.setTies((int)(Math.random()*100));

		return score;
                /* Why didn't we update the entityManager??? */
	}
	
	@GetMapping("/addabunchofscores")
	public List<Score> addSomeScores() {
		for (int i = 0; i<10;i++) {
			Score score = new Score();
			entityManager.persist(score);
			
			score.setWins((int)(Math.random()*100));
			score.setLosses((int)(Math.random()*100));
			score.setTies((int)(Math.random()*100));
            /* Why didn't we update the entityManager??? */
		}
		return getAllScores();
	}
	
	/* This will load the first record on Spring startup.*/
	@EventListener(ContextRefreshedEvent.class)
	public void addTheFirstRecordOnStartup() {
		Score score = entityManager.find(Score.class, 1);
		if (score == null) {
			score = new Score();
			entityManager.persist(score);
		}
		System.out.println("Score object id 1 created.");
	}
}
