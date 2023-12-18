package com.example.custom.test.service;

import com.example.custom.test.entity.City;
import com.example.custom.test.entity.Country;
import com.example.custom.test.entity.Firm;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class TestServiceImpl implements  TestService{

    private String CITIES_NAME = " Бобруйск Дубровно Борисов Жлобин Витебск Калинковичи Гомель Климовичи Минск Костюковичи Могилёв Кричев Мозырь Лепель Орша Мстиславль Полоцк Осиповичи Речица Петриков Слуцк Рогачёв Быхов Сенно Ветка Хрень Зорька Москва Столичин Волгоград Новогрудок";
    private String FIRMS_NAME = "АЛЬЯНС, ВЕКТОР, ФЕНИКС, ОРИОН, ЛИДЕР, ПРОГРЕСС, ВИКТОРИЯ, АВАНГАРД, СПЕКТР, ФОРТУНА, МЕРКУРИЙ, ОМЕГА, ПАРТНЕР, ПЕРСПЕКТИВА, ФАВОРИТ, АЛЬФА, НАДЕЖДА, ГЕРМЕС, ОЛИМП, СФЕРА, КАСКАД, ДЕЛЬТА, ГАРАНТ, СИРИУС, НИКА, МЕРИДИАН, СТРОЙСЕРВИС, ВЕГА, МОНОЛИТ, ИМПУЛЬС, РАДУГА, ПРЕСТИЖ, ВЕЛЕС, РЕСУРС, ВОСТОК, ВЕСТА, АТЛАНТ, ГРАНД, КОНТИНЕНТ, МЕГАПОЛИС, СОЮЗ, КРИСТАЛЛ, СТАНДАРТ, ВОСХОД, АЛЬТАИР, ТРИУМФ, УСПЕХ, ВЕРТИКАЛЬ, СТАТУС, САТУРН, ПАРИТЕТ, ГОРИЗОНТ, СИГМА, РАССВЕТ, ЭВЕРЕСТ, БРИЗ, ОНИКС, ОПТИМА, АВРОРА, ГРАНИТ, СТРОЙИНВЕСТ, ЭДЕЛЬВЕЙС, СТРОИТЕЛЬ, АЛЬТЕРНАТИВА, ЭТАЛОН, МАСТЕР, АЗИМУТ, СТРОЙКОМПЛЕКТ, ПАРУС, ЛЕГИОН, СТАРТ, ЛУЧ, ЭНЕРГИЯ, ЛОТОС, ТИТАН, РЕГИОН, СТИМУЛ, УНИВЕРСАЛ, ИМПЕРИЯ, КОМФОРТ, РУБИН, СПУТНИК, ВОЗРОЖДЕНИЕ, БАЗИС, ЗАРЯ, КАПИТАЛ, ИСТОК, МАГИСТРАЛЬ, АРСЕНАЛ, ЭКСПЕРТ, АГАТ, КОНТУР, АБСОЛЮТ, ЮПИТЕР, КРОНА, АСТРА, ОРБИТА, КЕДР, СЕВЕР, ТАНДЕМ, ПРОФИТ, ГЛОБУС, КОЛОС, РАЗВИТИЕ";
    private String COUNTRIES_NAME = "Эстония Эфиопия ЮАР Южная Осетия Южный Судан Ямайка Япония";

    public List<Country> getCountries() {
        Random rand = new Random();
        List<Country> result = new ArrayList<>();
        List<String> countries = List.of(COUNTRIES_NAME.split(" "));
        List<String> cities = List.of(CITIES_NAME.split(" "));
        List<String> firms = List.of(FIRMS_NAME.split(", "));
        int cityI = 0;
        int firmI = 0;
        for (String country : countries) {
            int citySize = rand.nextInt(3) + 1 + cityI;
            List<City> countryCities = new ArrayList<>();

            for (; cityI < citySize; cityI++) {

                int firmSize = rand.nextInt(3) + 1 + firmI;
                List<Firm> cityFirms = new ArrayList<>();

                for (;firmI < firmSize; firmI++) {
                    cityFirms.add(Firm.builder().name(firms.get(firmI)).build());
                }
                countryCities.add(City.builder().name(cities.get(cityI)).firms(cityFirms).build());
            }
            result.add(Country.builder().cities(countryCities).name(country).build());
        }

        return result;
    }
}
