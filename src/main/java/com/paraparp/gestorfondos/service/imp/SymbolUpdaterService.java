package com.paraparp.gestorfondos.service.imp;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paraparp.gestorfondos.model.entity.Symbol;
import com.paraparp.gestorfondos.repository.ISymbolRepository;
import com.paraparp.gestorfondos.service.IMorningStartService;
import com.paraparp.gestorfondos.util.ExtraData;
import com.paraparp.gestorfondos.util.Util;

@Service
public class SymbolUpdaterService {

	@Autowired
	private ISymbolRepository respository;
	
	@Autowired
	private MorningStarService msService;

	public String updater() throws IOException, JSONException {

		List<Symbol> symbols = respository.findAll();

		int updated = 0;

		for (Symbol symbol : symbols) {

			ExtraData bd = null;
			try {
				bd = Util.getFTData(symbol.getIsin());
			} catch (Exception e) {

				bd = null;
			}

			if (bd != null) {
				symbol.setName(bd.getName());
				symbol = fillObject(symbol, bd);

				updated++;
			} else {

				System.out.println(symbol.getIsin() + " - " + symbol.getName() + ": Failed Update. ");
			}
		}
		respository.saveAll(symbols);
		
		msService.getAllSymbolsHistorical(); //TODO actualizamos historiales

		return "Symbols : " + symbols.size() + " ===> Updated :  " + updated;
	}

	public Symbol searchByIsin(String isin) {

		// TODO por si el simbolo ya existe
		Symbol symbolBack = respository.findByIsin(isin);

		if (symbolBack != null) {
			System.out.println("Symbol already exists " + symbolBack.getIsin());
			return null;//TODO hay que lanza error
		}

		ExtraData bd = null;
		try {
			bd = Util.getFTData(isin);
		} catch (Exception e) {
			return null;
		}

		if (bd != null) {

			Symbol symbol = new Symbol();
			fillObject(symbol, bd);

			return symbol;
		}
		return null;

	}

	private Symbol fillObject(Symbol symbol, ExtraData bd) {
		symbol.setName(bd.getName());
		symbol.setIsin(bd.getIsin());
		symbol.setLastPrice(bd.getLastPrice());
		symbol.setLastDate(bd.getUpdatedAt());
		symbol.setUpdated(LocalDate.now());
		symbol.setCategory(bd.getCategory());
		symbol.setType(bd.getType());
		symbol.setLocation(bd.getLocation());
		symbol.setUrl(bd.getUrl());
		symbol.setDailyChange(bd.getDailyChange());
		symbol.setDailyChangePercent(bd.getDailyChangePercent());
		symbol.setOneYear(bd.getOneYear());
		symbol.setFiveYears(bd.getFiveYears());

		return symbol;
	}
}
