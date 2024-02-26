package com.example.custom.tier;

import com.example.custom.entity.Arrival;
import com.example.custom.entity.Consumption;
import com.example.custom.exception.TierException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.custom.util.MockUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ConsumptionTierTest {

    @Autowired
    private ConsumptionTier consumptionTier;

    @Test
    public void tie_ShouldThrowTierException_WhenArrivalNameNull() {
        List<Arrival> arrivals = new ArrayList<>();
        arrivals.add(createArrival(null, 20));
        List<Consumption> consumptions = new ArrayList<>();

        assertThrows(TierException.class, () -> consumptionTier.tie(arrivals, consumptions, createNonArrival()));
    }

    @Test
    public void tie_ShouldThrowTierException_WhenArrivalStoreHouseNull() {
        List<Arrival> arrivals = new ArrayList<>();
        arrivals.add(createArrival("null", 20, .1, null, LocalDate.now()));
        List<Consumption> consumptions = new ArrayList<>();

        assertThrows(TierException.class, () -> consumptionTier.tie(arrivals, consumptions, createNonArrival()));
    }

    @Test
    public void tie_ShouldThrowTierException_WhenArrivalDateNull() {
        List<Arrival> arrivals = new ArrayList<>();
        Arrival arrival = createArrival("1", 20);
        arrival.setArrivalDate(null);
        arrivals.add(arrival);
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 15));

        assertThrows(TierException.class, () -> consumptionTier.tie(arrivals, consumptions, createNonArrival()));
    }


    @Test
    public void tie_ShouldReturnTierArrivalsWithConsumptions() {
        List<Arrival> expected = createReadyArrivals();
        expected.add(createNonArrival());

        List<Arrival> arrivals = createArrivals();
        List<Consumption> consumptions = createConsumptions();
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void tie_ShouldReturnTierArrivalsWithConsumptions_WhenEmptyConsumptions() {
        List<Arrival> expected = createArrivals();
        expected.add(createNonArrival());

        List<Arrival> arrivals = createArrivals();
        List<Consumption> consumptions = new ArrayList<>();
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void tie_ShouldReturnTierArrivalsWithConsumptions_WhenEmptyArrivals() {
        List<Arrival> expected = new ArrayList<>();
        Arrival arrival = createNonArrival();
        arrival.setConsumptions(createConsumptions());
        expected.add(arrival);

        List<Arrival> arrivals = new ArrayList<>();
        List<Consumption> consumptions = createConsumptions();
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void tie_ShouldReturnTierArrivalsWithConsumptions_WhenEmptyArrivalsAndConsumptions() {
        List<Arrival> expected = new ArrayList<>();
        expected.add(createNonArrival());

        List<Arrival> arrivals = new ArrayList<>();
        List<Consumption> consumptions = new ArrayList<>();
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void tie_ShouldReturnTierArrivalsWithConsumptions_WhenTotalConsumptionCountLess() {
        List<Arrival> expected = new ArrayList<>();
        expected.add(createArrival("1", 20, new ArrayList<>(List.of(
                createConsumption("1", 15),
                createConsumption("1", 5)
        ))));
        expected.add(createArrival("1", 20, new ArrayList<>(List.of(
                createConsumption("1", 15)
        ))));
        expected.add(createNonArrival());

        List<Arrival> arrivals = new ArrayList<>();
        arrivals.add(createArrival("1", 20));
        arrivals.add(createArrival("1", 20));
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 15));
        consumptions.add(createConsumption("1", 20));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void tie_ShouldReturnTierArrivalsWithConsumptions_WhenArrivalCountLess() {
        List<Arrival> expected = new ArrayList<>();
        expected.add(createArrival("1", 15, new ArrayList<>(List.of(
                createConsumption("1", 15)
        ))));
        expected.add(createArrival("1", 20, new ArrayList<>(List.of(
                createConsumption("1", 5),
                createConsumption("1", 15)
        ))));
        expected.add(createNonArrival());

        List<Arrival> arrivals = new ArrayList<>();
        arrivals.add(createArrival("1", 15));
        arrivals.add(createArrival("1", 20));
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 20));
        consumptions.add(createConsumption("1", 15));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void tie_ShouldReturnTierArrivalsWithConsumptions_WhenConsumptionCountLess() {
        List<Arrival> expected = new ArrayList<>();
        expected.add(createArrival("1", 20, new ArrayList<>(List.of(
                createConsumption("1", 15),
                createConsumption("1", 5)
        ))));
        expected.add(createArrival("1", 15, new ArrayList<>(List.of(
                createConsumption("1", 15)
        ))));
        expected.add(createNonArrival());

        List<Arrival> arrivals = new ArrayList<>();
        arrivals.add(createArrival("1", 20));
        arrivals.add(createArrival("1", 15));
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 15));
        consumptions.add(createConsumption("1", 20));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void tie_ShouldReturnTierArrivalsWithConsumptions_WhenConsumptionCountEqualsLastArrivalCount() {
        List<Arrival> expected = new ArrayList<>();
        expected.add(createArrival("1", 20, new ArrayList<>(List.of(
                createConsumption("1", 20)
        ))));
        expected.add(createArrival("1", 15, new ArrayList<>(List.of(
                createConsumption("1", 10),
                createConsumption("1", 5)
        ))));
        expected.add(createNonArrival());

        List<Arrival> arrivals = new ArrayList<>();
        arrivals.add(createArrival("1", 20));
        arrivals.add(createArrival("1", 15));
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 20));
        consumptions.add(createConsumption("1", 10));
        consumptions.add(createConsumption("1", 5));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void tie_ShouldReturnTierArrivalsWithConsumptions_WhenConsumptionCountLessAndStartCountInArrival() {
        List<Arrival> expected = new ArrayList<>();
        expected.add(createArrival("1", 20, new ArrayList<>(List.of(
                createConsumption("1", 5),
                createConsumption("1", 15)
        ))));
        expected.add(createArrival("1", 20, new ArrayList<>(List.of(
                createConsumption("1", 20)
        ))));
        expected.add(createNonArrival());

        List<Arrival> arrivals = new ArrayList<>();
        Arrival arrival = createArrival("1", 20);
        arrival.setConsumptions(new ArrayList<>(List.of(createConsumption("1", 5))));
        arrivals.add(arrival);
        arrivals.add(createArrival("1", 20));
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 15));
        consumptions.add(createConsumption("1", 20));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void tie_ShouldReturnTierArrivalsWithConsumptions_WhenArrivalCountLessAndStartCountInArrival() {
        List<Arrival> expected = new ArrayList<>();
        expected.add(createArrival("1", 20, new ArrayList<>(List.of(
                createConsumption("1", 5),
                createConsumption("1", 15)
        ))));
        expected.add(createArrival("1", 30, new ArrayList<>(List.of(
                createConsumption("1", 5),
                createConsumption("1", 20)
        ))));
        expected.add(createNonArrival());

        List<Arrival> arrivals = new ArrayList<>();
        Arrival arrival = createArrival("1", 20);
        arrival.setConsumptions(new ArrayList<>(List.of(createConsumption("1", 5))));
        arrivals.add(arrival);
        arrivals.add(createArrival("1", 30));
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 20));
        consumptions.add(createConsumption("1", 20));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void tie_ShouldReturnConsumptionsTotalMoreThanArrival_WhenLastConsumptionIsEqual() {
        List<Arrival> expected = new ArrayList<>();
        expected.add(createArrival("1", 15, new ArrayList<>(List.of(
                createConsumption("1", 15)
        ))));
        expected.add(createArrival("1", 20, new ArrayList<>(List.of(
                createConsumption("1", 5),
                createConsumption("1", 15)
        ))));
        Arrival nonArrival = createNonArrival();
        nonArrival.setConsumptions(new ArrayList<>(List.of(createConsumption("1", 15))));
        expected.add(nonArrival);

        List<Arrival> arrivals = new ArrayList<>();
        arrivals.add(createArrival("1", 15));
        arrivals.add(createArrival("1", 20));
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 20));
        consumptions.add(createConsumption("1", 15));
        consumptions.add(createConsumption("1", 15));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void tie_ShouldReturnConsumptionsTotalMoreThanArrival_WhenLastConsumptionIsLess() {
        List<Arrival> expected = new ArrayList<>();
        expected.add(createArrival("1", 15, new ArrayList<>(List.of(
                createConsumption("1", 15)
        ))));
        expected.add(createArrival("1", 20, new ArrayList<>(List.of(
                createConsumption("1", 5),
                createConsumption("1", 13),
                createConsumption("1", 2)
        ))));
        Arrival nonArrival = createNonArrival();
        nonArrival.setConsumptions(new ArrayList<>(List.of(createConsumption("1", 13))));
        expected.add(nonArrival);

        List<Arrival> arrivals = new ArrayList<>();
        arrivals.add(createArrival("1", 15));
        arrivals.add(createArrival("1", 20));
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 20));
        consumptions.add(createConsumption("1", 13));
        consumptions.add(createConsumption("1", 15));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void tie_ShouldReturnConsumptionsTotalMoreThanArrival_WhenLastConsumptionIsMore() {
        List<Arrival> expected = new ArrayList<>();
        expected.add(createArrival("1", 15, new ArrayList<>(List.of(
                createConsumption("1", 15)
        ))));
        expected.add(createArrival("1", 20, new ArrayList<>(List.of(
                createConsumption("1", 5),
                createConsumption("1", 15)
        ))));
        Arrival nonArrival = createNonArrival();
        nonArrival.setConsumptions(new ArrayList<>(List.of(createConsumption("1", 2),
                createConsumption("1", 15
                ))));
        expected.add(nonArrival);

        List<Arrival> arrivals = new ArrayList<>();
        arrivals.add(createArrival("1", 15));
        arrivals.add(createArrival("1", 20));
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 20));
        consumptions.add(createConsumption("1", 17));
        consumptions.add(createConsumption("1", 15));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

  @Test
    public void tie_ShouldReturnConsumptionsTotalMoreThanArrival_WhenOneBigConsumption() {
        List<Arrival> expected = new ArrayList<>();
        expected.add(createArrival("1", 20, new ArrayList<>(List.of(
                createConsumption("1", 20)
        ))));
        expected.add(createArrival("1", 20, new ArrayList<>(List.of(
                createConsumption("1", 20)
        ))));
        expected.add(createArrival("1", 20, new ArrayList<>(List.of(
                createConsumption("1", 20)
        ))));
        expected.add(createArrival("1", 18, new ArrayList<>(List.of(
                createConsumption("1", 18)
        ))));

        Arrival nonArrival = createNonArrival();
        nonArrival.setConsumptions(new ArrayList<>(List.of(
                createConsumption("1", 22
                ))));
        expected.add(nonArrival);

        List<Arrival> arrivals = new ArrayList<>();
        arrivals.add(createArrival("1", 20));
        arrivals.add(createArrival("1", 20));
        arrivals.add(createArrival("1", 20));
        arrivals.add(createArrival("1", 18));
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 100));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

  @Test
    public void tie_ShouldReturnConsumptionsTotalMoreThanArrival_WhenOnlyConsumptionInCache() {
        List<Arrival> expected = new ArrayList<>();

        Arrival nonArrival = createNonArrival();
        nonArrival.setConsumptions(new ArrayList<>(List.of(
                createConsumption("1", 100),
                createConsumption("1", 100)
                )));
        expected.add(nonArrival);

        List<Arrival> arrivals = new ArrayList<>();
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 100));
        consumptions.add(createConsumption("1", 100));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

  @Test
    public void tie_ShouldReturnConsumptionsTotalMoreThanArrival_WhenOnlyConsumptionInCacheWithCorrectives() {
        List<Arrival> expected = new ArrayList<>();

        Arrival nonArrival = createNonArrival();
        nonArrival.setConsumptions(new ArrayList<>(List.of(
                )));
        expected.add(nonArrival);

        List<Arrival> arrivals = new ArrayList<>();
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 100));
        consumptions.add(createConsumption("1", -100));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

  @Test
    public void tie_ShouldReturnConsumptionsTotalMoreThanArrival_WhenOnlyConsumptionInCacheWithMoreCorrectives() {
        List<Arrival> expected = new ArrayList<>();

        Arrival nonArrival = createNonArrival();
        nonArrival.setConsumptions(new ArrayList<>(List.of(
                )));
        expected.add(nonArrival);

        List<Arrival> arrivals = new ArrayList<>();
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 100));
        consumptions.add(createConsumption("1", -100));
        consumptions.add(createConsumption("1", -100));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void tie_ShouldReturnConsumptionsTotalMoreThanArrival_WhenOnlyConsumptionInCacheWithLessCorrectives() {
        List<Arrival> expected = new ArrayList<>();

        Arrival nonArrival = createNonArrival();
        nonArrival.setConsumptions(new ArrayList<>(List.of(
                createConsumption("1", 100)
        )));
        expected.add(nonArrival);

        List<Arrival> arrivals = new ArrayList<>();
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 100));
        consumptions.add(createConsumption("1", 100));
        consumptions.add(createConsumption("1", -100));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

  @Test
    public void tie_ShouldReturnConsumptionsTotalMoreThanArrival_WhenOnlyConsumptionInCacheWithManyCorrectives() {
        List<Arrival> expected = new ArrayList<>();

        Arrival nonArrival = createNonArrival();
        nonArrival.setConsumptions(new ArrayList<>(List.of(
                createConsumption("1", 100)
        )));
        expected.add(nonArrival);

        List<Arrival> arrivals = new ArrayList<>();
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 100));
        consumptions.add(createConsumption("1", 100));
        consumptions.add(createConsumption("1", 100));
        consumptions.add(createConsumption("1", -100));
        consumptions.add(createConsumption("1", -100));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }


    @Test
    public void tie_ShouldReturnConsumptionsTotalMoreThanArrival_WhenLastConsumptionIsLittleMore() {
        List<Arrival> expected = new ArrayList<>();
        expected.add(createArrival("1", 15, new ArrayList<>(List.of(
                createConsumption("1", 15)
        ))));
        expected.add(createArrival("1", 20, new ArrayList<>(List.of(
                createConsumption("1", 20)
        ))));
        Arrival nonArrival = createNonArrival();
        nonArrival.setConsumptions(new ArrayList<>(List.of(
                createConsumption("1", 1
                ))));
        expected.add(nonArrival);

        List<Arrival> arrivals = new ArrayList<>();
        arrivals.add(createArrival("1", 15));
        arrivals.add(createArrival("1", 20));
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 15));
        consumptions.add(createConsumption("1", 21));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

  @Test
    public void tie_ShouldReturnConsumptionsTotalMoreThanArrival_WhenLastConsumptionIsEquals() {
        List<Arrival> expected = new ArrayList<>();
        expected.add(createArrival("1", 40, new ArrayList<>(List.of(
                createConsumption("1", 40)
        ))));
        Arrival nonArrival = createNonArrival();
        nonArrival.setConsumptions(new ArrayList<>(List.of(
                createConsumption("1", 50
                ))));
        expected.add(nonArrival);

        List<Arrival> arrivals = new ArrayList<>();
        arrivals.add(createArrival("1", 40));
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 40));
        consumptions.add(createConsumption("1", 50));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void tie_ShouldReturnTierArrivalsWithConsumptions_WhenConsumptionDateLess() {
        List<Arrival> expected = new ArrayList<>();
        expected.add(createArrival("1", 20, new ArrayList<>(List.of(
                createConsumption("1", 15),
                createConsumption("1", 5)
        ))));
        expected.add(createArrival("1", 20, new ArrayList<>(List.of(
                createConsumption("1", 15)
        ))));
        Arrival nonArrival = createNonArrival();
        Consumption expectConsumptionLessDate = createConsumption("1", 30);
        expectConsumptionLessDate.setArrivalDate(LocalDate.now().minusDays(1l));
        nonArrival.setConsumptions(new ArrayList<>(List.of(expectConsumptionLessDate)));
        expected.add(nonArrival);

        List<Arrival> arrivals = new ArrayList<>();
        arrivals.add(createArrival("1", 20));
        arrivals.add(createArrival("1", 20));
        List<Consumption> consumptions = new ArrayList<>();
        Consumption consumptionLessDate = createConsumption("1", 30);
        consumptionLessDate.setArrivalDate(LocalDate.now().minusDays(1l));
        consumptions.add(consumptionLessDate);
        consumptions.add(createConsumption("1", 15));
        consumptions.add(createConsumption("1", 20));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }


    @Test
    public void tie_ShouldReturnTierArrivals_WhenArrivalsWithCorrectives() {
        List<Arrival> expected = new ArrayList<>();
        expected.add(createNonArrival());

        List<Arrival> arrivals = new ArrayList<>();
        arrivals.add(createArrival("1", 15));
        arrivals.add(createArrival("1", -15));
        List<Consumption> consumptions = new ArrayList<>();
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void tie_ShouldReturnTierArrivals_WhenArrivalsWithCorrectivesMoreLessThanZero() {
        List<Arrival> expected = new ArrayList<>();
        expected.add(createNonArrival());

        List<Arrival> arrivals = new ArrayList<>();
        arrivals.add(createArrival("1", 15));
        arrivals.add(createArrival("1", -15));
        arrivals.add(createArrival("1", -15));
        List<Consumption> consumptions = new ArrayList<>();
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void tie_ShouldReturnTierArrivals_WhenArrivalsWithCorrectivesLessThanZero() {
        List<Arrival> expected = new ArrayList<>();
        expected.add(createArrival("1", 15));
        expected.add(createNonArrival());

        List<Arrival> arrivals = new ArrayList<>();
        arrivals.add(createArrival("1", 15));
        arrivals.add(createArrival("1", 15));
        arrivals.add(createArrival("1", -15));
        List<Consumption> consumptions = new ArrayList<>();
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void tie_ShouldReturnTierArrivals_WhenConsumptionsWithCorrectives() {
        List<Arrival> expected = new ArrayList<>();
        Arrival nonArrival = createNonArrival();
        nonArrival.setConsumptions(new ArrayList<>());
        expected.add(nonArrival);

        List<Arrival> arrivals = new ArrayList<>();
        arrivals.add(createArrival("1", 15));
        arrivals.add(createArrival("1", -15));
        List<Consumption> consumptions = new ArrayList<>();
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void tie_ShouldReturnTierArrivals_WhenArrivalsWithConsumptionMoreLessThanZero() {
        List<Arrival> expected = new ArrayList<>();
        Arrival nonArrival = createNonArrival();
        nonArrival.setConsumptions(new ArrayList<>());
        expected.add(nonArrival);

        List<Arrival> arrivals = new ArrayList<>();
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 21));
        consumptions.add(createConsumption("1", -21));
        consumptions.add(createConsumption("1", -21));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }

    @Test
    public void tie_ShouldReturnTierArrivals_WhenArrivalsWithConsumptionsLessThanZero() {
        List<Arrival> expected = new ArrayList<>();
        Arrival nonArrival = createNonArrival();
        nonArrival.setConsumptions(new ArrayList<>(List.of(createConsumption("1", 15))));
        expected.add(nonArrival);

        List<Arrival> arrivals = new ArrayList<>();
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(createConsumption("1", 15));
        consumptions.add(createConsumption("1", 15));
        consumptions.add(createConsumption("1", -15));
        List<Arrival> actual = consumptionTier.tie(arrivals, consumptions, createNonArrival());

        assertEquals(expected.size(), actual.size());
        assertIterableEquals(expected, actual);
    }
}
