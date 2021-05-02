package kz.fis.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ShowsReader {

    private static final String BASE_URL = "https://www.tiobe.com/tiobe-index/";

    // Парсинг котировок из формата html web-страницы банка, при ошибке доступа возвращаем null
    public static String getShowsData() {
        StringBuilder data = new StringBuilder();
        try {
            Document doc = Jsoup.connect(BASE_URL).timeout(5000).get(); // Создание документа JSOUP из html
            data.append("index | TIOBE\n");
            data.append(String.format("%12s\n", "Топ 20 языков программирования:").trim());
            data.append("\n");
            Elements section = doc.select("section.clearfix"); // Ищу в документе секцию "<section class="clearfix"> с данными о языках программирования
            Elements articles = section.select("article.whitebg"); // Ищу статью, которая принадлежит классу "whitebg"
            Element article = articles.get(0); // Выибраю первую статью
            Elements tables = article.select("table.table"); // Ищу таблицу, которая принадлежит классу "table"
            Element table = tables.get(0); // Выибраю первую таблицу
            Elements tHeads = table.select("thead"); // Ищу заглавье таблицы
            Element tHead = tHeads.get(0); // Выибраю первое заглавье таблицы
            Elements months = tHead.select("tr");
            Elements th = months.select("th");
            Element monthNow = th.get(0); // Выибраю первый столбец таблицы с датой, которая была год назад
            Element monthBefore = th.get(1);// Выибраю второй столбец таблицы с нынешней датой

            Elements tBodies = table.select("tbody"); // Ищу тело таблицы
            Element tBody = tBodies.get(0); // Выибраю первое тело таблицы

            for (Element row : tBody.select("tr")) {// Цикл по столбцам таблицы
                Elements td = row.select("td");
                Element tdNow = td.get(0); // Выибраю первый столбец таблицы, в котором содержится место языка программрования в топ на данный момент
                Element tdBefore = td.get(1); // Выибраю второй столбец с местом языка программрования в топе год назад
                Element tdLang = td.get(3); // Выбираю третий столбец с названием языка программирование
                data.
                        append("№").append(tdNow.text()).append("\n"). //Выведет №(место языка программирования на данный момент) и переведет на новую строку
                        append("Programming Language: ").append(tdLang.text()).append("\n"). //Выведет Programming Language: (название языка программирования) и переведет на новую строку
                        append(monthNow.text()).append(": ").append(tdNow.text()).append("\n"). //Выведет нынейшний месяц и год, место которое занимает ЯП на данный момент и переведет на новую строку
                        append(monthBefore.text()).append(": ").append(tdBefore.text()).append("\n\n"); //Выведет месяц и год, что были год назад, затем место которое занимал ЯП год назад и переведет на две новую строку
            }
        } catch (Exception ignored) {
            return null; // При ошибке доступа возвращаем null
        }
        return data.toString().trim(); // Возвращаем результат
    }

}