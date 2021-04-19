// This is a personal academic project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com

package ru.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.models.Client;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** В абстрактном классе DAO реализуется логика работы с базой данных. В нем содержатся CRUD методы **/

@Repository
public class ClientsDao {

    private final JdbcTemplate jdbcTemplate;

    public ClientsDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /** RowMapper - объект, отображающий строки из таблицы в нашей сущности */
    public List<Client> index() {
        return jdbcTemplate.query("SELECT * FROM Client ORDER BY id", new BeanPropertyRowMapper<>(Client.class));
    }

    /** Статус клиента может быть в двух состояниях(тип Boolean), если статус активный, то при нажатии на соответсвующее
    поле мы переключаем статус из акетивного состояния в неактивное и наоборот. В данном методе checkbox мы  получаем
    list в котором содержится единственный обьект из таблицы со значением переданного id. Далее создаем паттерн, который
    должен находить строку со статусом. В matcher.find находится значение статуса и далее записывается в строку статус.
    После мы сравниваем с помощью метода equals значение статуса с его предполагаемыми значениями. Если статус null или
    false , поле в таблице изменяется на противоположное,т.е true (также наоборот). Я думаю, что менять статус можно
    проще, но еще не узнал как.
     **/

    public void checkbox(int id) {
        List<Client> list = jdbcTemplate.query("SELECT * FROM Client WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Client.class));
        String ArrayToString = list.toString();
        String status = "something";
        Pattern pattern = Pattern.compile("status=[a-zA-Z]+[a-zA-Z]");
        Matcher matcher = pattern.matcher(ArrayToString);
        while (matcher.find()) {
            status = ArrayToString.substring(matcher.start(), matcher.end());
        }
        if (status.equals("status=null") || status.equals("status=false"))
            jdbcTemplate.update("UPDATE Client SET status=? WHERE id=?", true, id);
        if (status.equals("status=true"))
            jdbcTemplate.update("UPDATE Client SET status=? WHERE id=?", false, id);
    }

    public Client editClient(int id) {
        return jdbcTemplate.query("SELECT * FROM client  WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Client.class))
                .stream().findAny().orElse(null);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM Client WHERE id=?", id);
    }

    public void updateClient(int id, Client client) {
        jdbcTemplate.update("UPDATE client SET name=? WHERE id=?", client.getName(), id);
    }
}