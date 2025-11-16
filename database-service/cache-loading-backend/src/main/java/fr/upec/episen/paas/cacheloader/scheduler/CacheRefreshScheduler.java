package fr.upec.episen.paas.cacheloader.scheduler;

import fr.upec.episen.paas.cacheloader.model.People;
import fr.upec.episen.paas.cacheloader.model.Student;
import fr.upec.episen.paas.cacheloader.repository.PeopleRepository;
import fr.upec.episen.paas.cacheloader.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@EnableScheduling
public class CacheRefreshScheduler {

    private final PeopleRepository peopleRepository;
    private final RedisService redisService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static boolean dbHasBeenUpdated = false;

    private static final String DOUBLE_QUOTE = "\"";

    private static final String FIRSTNAME = DOUBLE_QUOTE + "firstname" +DOUBLE_QUOTE;
    private static final String LASTNAME = DOUBLE_QUOTE + "lastname" +DOUBLE_QUOTE;
    private static final String ISAUTHORISED = DOUBLE_QUOTE + "isAuthorized" +DOUBLE_QUOTE;
    private static final String NUM = DOUBLE_QUOTE + "num" +DOUBLE_QUOTE;



    @Autowired
    public CacheRefreshScheduler(PeopleRepository peopleRepository, RedisService redisService) {
        this.peopleRepository = peopleRepository;
        this.redisService = redisService;
    }

    /**
     * Exécute la tâche toutes les 30 secondes (30000 ms)
     * Vous pouvez ajuster l'intervalle selon vos besoins :
     * - 60000 ms = 1 minute
     * - 300000 ms = 5 minutes
     * - 900000 ms = 15 minutes
     */
    @Scheduled(fixedRate = 30000)
    private void refreshPeopleCache() {
        if (!dbHasBeenUpdated) {
            return;
        }
        try {
            System.out.println("[Scheduler] " + LocalDateTime.now().format(formatter) + " - Rafraîchissement du cache Redis...");
            
            // Récupère les personnes autorisées (équivalent de getPersonAllowed())
            List<People> allowedPeople = peopleRepository.findAllAllowedNow();
            List<People> deniedPeople = peopleRepository.findAllNotAllowedNow();
            
            // TODO: Mapper (stream?) sur des hashmap (cf doc redis) avec pour clé l'ID
            Map<Long, Map<String, String>> lmap = new HashMap<>();

            for (People people : allowedPeople) {
                Map<String, String> ap = new HashMap<>();
                ap.put(FIRSTNAME, DOUBLE_QUOTE+people.getFirstName()+DOUBLE_QUOTE);
                ap.put(LASTNAME, DOUBLE_QUOTE+people.getLastName()+DOUBLE_QUOTE);
                ap.put(ISAUTHORISED, DOUBLE_QUOTE+"true"+DOUBLE_QUOTE);
                ap.put(NUM, DOUBLE_QUOTE+people.getNum()+DOUBLE_QUOTE);
                lmap.put(people.getId(), ap);
            }

            for (People people : deniedPeople) {
                Map<String, String> ap = new HashMap<>();
                ap.put(FIRSTNAME, DOUBLE_QUOTE+people.getFirstName()+DOUBLE_QUOTE);
                ap.put(LASTNAME, DOUBLE_QUOTE+people.getLastName()+DOUBLE_QUOTE);
                ap.put(ISAUTHORISED, DOUBLE_QUOTE+"false"+DOUBLE_QUOTE);
                ap.put(NUM, DOUBLE_QUOTE+people.getNum()+DOUBLE_QUOTE);
                lmap.put(people.getId(), ap);
            }

            
            // Écrit dans Redis
            redisService.saveAllowedPeople(lmap);
            
            System.out.println("[Scheduler] ✓ Cache mis à jour avec " + lmap.size() + " personnes");
            dbHasBeenUpdated = false;
        } catch (Exception e) {
            System.err.println("[Scheduler] ✗ Erreur lors du rafraîchissement : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Optionnel : Tâche initiale au démarrage de l'application
     * Attends 5 secondes avant la première exécution
     */
    @Scheduled(initialDelay = 5000)
    private void initializeCache() {
        dbHasBeenUpdated = true;
        System.out.println("[Scheduler] Application démarrée, initialisation du cache...");
        refreshPeopleCache();
    }

    public void tableHasBeenUpdated() {
        dbHasBeenUpdated = true;
    }
}
