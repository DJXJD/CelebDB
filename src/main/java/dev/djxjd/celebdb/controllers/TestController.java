package dev.djxjd.celebdb.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestController {
	
	@GetMapping("/")
	public String getRoot(@RequestParam(required = false) String name, Model model) {
		if (name != null) {
			Document wikiPage;
			try {
				wikiPage = Jsoup.connect("https://en.wikipedia.org/wiki/" + name).get();
			} catch (IOException e) {
				wikiPage = new Document("shit fucked up");
				e.printStackTrace();
			}
			Element bday = wikiPage.selectFirst(".bday");
			if (bday == null) model.addAttribute("error", name + "'s birthday could not be found");
			else {
				model.addAttribute("bday", LocalDate.parse(bday.text()).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
				model.addAttribute("image", wikiPage.selectFirst(".infobox-image img").attr("src"));
				model.addAttribute("name", wikiPage.selectFirst("h1 span").text());
			}
		}
		return "scrapeBDay";
	}

}
