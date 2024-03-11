package dev.djxjd.celebdb.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TestController {
	
	@GetMapping("/")
	public String getRoot() {
		return "scrapeBDay";
	}
	
	@PostMapping("/scrape")
	public String scrapeBDay(@RequestParam String name, RedirectAttributes ra) {
		Document wikiPage;
		try {
			wikiPage = Jsoup.connect("https://en.wikipedia.org/wiki/" + name).get();
		} catch (IOException e) {
			wikiPage = new Document("shit fucked up");
			e.printStackTrace();
		}
		Element bday = wikiPage.selectFirst(".bday");
		if (bday == null) ra.addFlashAttribute("error", name + "'s birthday could not be found");
		else {
			ra.addFlashAttribute("bday", LocalDate.parse(bday.text()).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)));
			ra.addFlashAttribute("image", wikiPage.selectFirst(".infobox-image img").attr("src"));
			ra.addFlashAttribute("name", wikiPage.selectFirst("h1 span").text());
		}
		return "redirect:/";
	}

}
