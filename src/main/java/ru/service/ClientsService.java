/**
Деталь (Газимьянов Д.И. Вариант 2)

        N10 G90 G54 G17 G94; Первичные установочные данные
        N20 T1 M6; Вызов и установка инструмента Т1 R215.34C08040-DC19K 1640 (фреза диам. 8 мм)
        N30 G00 X0 Y0 Z60; Перемещение в данные координаты на быстром ходу
        N40 S3000 M13; Включение шпинделя в 3000 об/мин и СОЖ
        N45 G41 D8 G43 H1; Компенасация длины и диаметры инструмента

        N50 X0 Y30 Z-05; Подвод инструмента к контуру
        N60 G01 X5 Y30; Начало процесса обработки контура
        N70 Y45; Обработка контура
        N80 X20 Y50; Обработка контура
        N90 X35; Обработка контура
        N100 X50 Y45; Обработка контура
        N110 Y40; Обработка контура
        N120 G03 X65 Y25 R15; Обработка скругления радиуса 15
        N130 G02 X75 Y15 R10; Обработка скругления радиуса 10
        N140 G01 Y10; Обработка контура
        N150 X70 Y5; Обработка контура
        N160 X30; Обработка контура
        N170 G02 X15 Y20 R15; Обработка скругления радиуса 15
        N180 G01 Y25; Обработка контура
        N190 G03 X10 Y30 R5; Обработка скругления радиуса 5
        N200 G01 X5 Y30; Обработка контура
        N210 X5 Y50; Обработка контура
        N220 M5 Z10; Выключение шпинделя и вывод фрезы из детали
        N230 G40 G49; Отмена коррекции
        N240 G28 X0 Y0 Z250; Возврат в исходную точку на быстром ходу

        N250 T2 M6; Вызов сверла диаметром 5
        N260 S3000 M3; Включение сверла в 3000 об/мин
        N270 G00 X10 Y40 Z20; Подведение сверла над первым отверстием
        N275 G01 Z3; Включение линейной интерполяции
        N280 G81 Z-10 R3 F300; Цикл сверления отверстий на глубине -10 с отводом инструмента на 3 над поверхностью детали и с заходом в 300 мм/мин
        N290 X25 Y40; Второе отверстие
        N300 X40 Y40; Третье отверстие
        N310 X45 Y10; Четвертое отверстие
        N320 X60 Y10; Пятое отверстие
        N330 G80 M5; Выключение цикла и сверла
        N340 G28 X0 Y0 Z250; Возврат в исходную точку на быстром ходу

        N350 T3 M6; Вызов метчика М6
        N360 S300 M3; Включение метчик в 300 об/мин
        N370 G00 X10 Y40 Z20; Подведение метчика над первым отверстием
        N375 G01; Включение линейной интерполяции
        N380 G95 G84 Z-10 R3 F1; Цикл нарезания отверстия на глубине -10 с отводом инструмента на 3 над поверхностью детали и с заходом в 1 мм/об
        N390 X25 Y40; Второе отверстие
        N400 X40 Y40; Третье отверстие
        N410 X45 Y10; Четвертое отверстие
        N420 X60 Y10; Пятое отверстие
        N430 G80 M5; Выключение цикла и метчика
        N440 G28 X0 Y0 Z250; Возврат в исходную точку на быстром ходу

        N450 T4 M6; Вызов сверла диаметром 15
        N460 S3000 M3; Включение сверла в 3000 об/мин
        N470 G00 X30 Y20 Z50; Перемещение сверла над отверстием
        N480 G94 G01 Z-10 F300; Перемещение сверла в деталь на высоту -10 с подачей в 300 мм/мин
        N490 Z20; Вывод сверла из детали
        N495 M5; Выключение сверла
        N500 G28 X0 Y0 Z250; Возврат в исходную точку на быстром ходу

        N510 M30; Завершение программы
 */

package ru.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.dao.ClientsDao;
import ru.models.Client;
import java.util.Collections;
import java.util.List;

@Service
public class ClientsService extends ClientsDao {

    public ClientsService(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public Page<Client> findPaginated(Pageable pageable) {
        List<Client> client = index();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Client> list;

        if (client.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, client.size());
            list = client.subList(startItem, toIndex);
        }

        return new PageImpl<Client>(list, PageRequest.of(currentPage, pageSize), client.size());
    }
}




