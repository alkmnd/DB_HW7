import com.github.javafaker.Faker;
import dao.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import tables.*;
import utils.HibernateSessionFactoryUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

class UniqueRng implements Iterator<Integer> {
    private List<Integer> numbers = new ArrayList<>();

    public UniqueRng(int n) {
        for (int i = 0; i < n; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);
    }

    @Override
    public Integer next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return numbers.remove(0);
    }

    @Override
    public boolean hasNext() {
        return !numbers.isEmpty();
    }
}

public class Main {
    static List<Country> countries = new ArrayList<>();
    static List<Olympic> olympics = new ArrayList<>();
    static Faker faker = new Faker();
    static List<Player> players = new ArrayList<>();
    static List<Event> events = new ArrayList<>();
    static List<ResultId> resIds = new ArrayList<>();
    static List<Result> results = new ArrayList<>();
    static Random rnd = new Random();

    static void insertCountries(int numberOfCountries) {
        CountryDAO cdao = new CountryDAO();
        Random rnd = new Random();
        UniqueRng rng = new UniqueRng(numberOfCountries);
        Country country;
        for (int i = 0; i < numberOfCountries; i++) {
            country = new Country();
            country.setId(rng.next().toString());
            country.setName(faker.country().name().split(" ")[0]);
            country.setAreaSqkm(rnd.nextInt(9900) + 100);
            country.setPopulation(rnd.nextInt(990000) + 10000);
            cdao.save(country);
            countries.add(country);
        }
    }

    static void insertOlympics(int numberOfOlympics) {
        Olympic olympic;
        OlympicDAO odao = new OlympicDAO();
        UniqueRng rng = new UniqueRng(numberOfOlympics);
        for (int i = 0; i < numberOfOlympics; i++) {
            olympic = new Olympic();
            olympic.setId(rng.next().toString());
            olympic.setCountry(countries.get(rnd.nextInt(countries.size())));
            olympic.setCity(faker.address().city().split(" ")[0]);
            int year = rnd.nextInt(32) + 1990;
            olympic.setYear(year);
            long minDay = LocalDate.of(year, 1, 1).toEpochDay();
            long maxDay = LocalDate.of(year, 12, 31).toEpochDay();
            long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
            LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
            olympic.setStartdate(randomDate);
            long dayEnd = ThreadLocalRandom.current().nextLong(randomDay, maxDay);
            olympic.setEnddate(LocalDate.ofEpochDay(dayEnd));
            odao.save(olympic);
            olympics.add(olympic);
        }
    }

    static void insertPlayers(int numberOfPlayers) {
        Player player;
        PlayerDAO pdao = new PlayerDAO();
        UniqueRng rng = new UniqueRng(numberOfPlayers);
        for (int i = 0; i < numberOfPlayers; i++) {
            player = new Player();
            player.setName(faker.superhero().name());
            player.setId(rng.next().toString());
            long minDay = LocalDate.of(1900, 1, 1).toEpochDay();
            long maxDay = LocalDate.of(2000, 12, 31).toEpochDay();
            long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
            LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
            player.setBirthdate(randomDate);
            player.setCountry(countries.get(rnd.nextInt(countries.size())));
            pdao.save(player);
            players.add(player);
        }
    }
    static void insertEvents(int numberOfEvents) {
        Event event;
        EventDAO edao = new EventDAO();
        UniqueRng rng = new UniqueRng(numberOfEvents);
        for (int i = 0; i < numberOfEvents; i++) {
            event = new Event();
            event.setId(rng.next().toString());
            event.setName(faker.esports().game());
            event.setEventtype(faker.leagueOfLegends().location());
            event.setIsTeamEvent(0);
            event.setOlympic(olympics.get(rnd.nextInt(olympics.size())));
            event.setNumPlayersInTeam(1);
            event.setResultNotedIn("Yes");
            edao.save(event);
            events.add(event);
        }
    }

    static void insertResults(int numberOfEvents) {
        Result res;
        ResultDAO rdao = new ResultDAO();
        UniqueRng rng = new UniqueRng(numberOfEvents);
        for (int i = 0; i < numberOfEvents; i++) {
            res = new Result();
            ResultId id = resIds.get(rng.next());
            res.setId(id);
            res.setEvent(events.get(Integer.parseInt(id.getEventId())));
            res.setPlayer(players.get(Integer.parseInt(id.getPlayerId())));
            res.setResult(0.0);
            List<String> medals = new ArrayList<>();
            medals.add("GOLD");
            medals.add("SILVER");
            medals.add("BRONZE");
            res.setMedal(medals.get(rnd.nextInt(3)));
            rdao.save(res);
            results.add(res);
        }
    }

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        System.out.print("Input a number of countries: ");
        int numberOfCountries = in.nextInt();
        insertCountries(numberOfCountries);

        System.out.print("Input a number of olympics: ");
        int numberOfOlympics = in.nextInt();
        insertOlympics(numberOfOlympics);

        System.out.print("Input a number of players: ");
        int numberOfPlayers = in.nextInt();
        insertPlayers(numberOfPlayers);

        System.out.print("Input a number of events: ");
        int numberOfEvents = in.nextInt();
        insertEvents(numberOfEvents);


        ResultId resId;
        UniqueRng rng = new UniqueRng(numberOfEvents);
        for (int i = 0; i < numberOfEvents; i++) {
            resId = new ResultId();
            resId.setPlayerId(players.get(rnd.nextInt(numberOfPlayers)).getId());
            resId.setEventId(events.get(rng.next()).getId());
            resIds.add(resId);
        }
        insertResults(numberOfEvents);
    }
}
