package com.paraparp.gestorfondos.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paraparp.gestorfondos.exception.ResourceNotFoundException;
import com.paraparp.gestorfondos.model.dto.LotDTO;
import com.paraparp.gestorfondos.model.dto.PortfolioDTO;
import com.paraparp.gestorfondos.model.dto.SymbolLotsDTO;
import com.paraparp.gestorfondos.model.entity.Portfolio;
import com.paraparp.gestorfondos.repository.IPortfolioRepository;
import com.paraparp.gestorfondos.service.IPortfolioService;
import com.paraparp.gestorfondos.service.ISymbolLotsService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/fundapp/portfolios")
public class PortfolioController {
	@Autowired
	private IPortfolioRepository portfolioRepository;

	@Autowired
	private IPortfolioService portfolioService;

	@Autowired
	private ISymbolLotsService symbolLotsService;

	@GetMapping("")
	public List<PortfolioDTO> getAllPortfolios() {
		return portfolioService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<PortfolioDTO> getPortfolioById(@PathVariable(value = "id") Long portfolioId)
			throws ResourceNotFoundException {
		PortfolioDTO portfolio = this.checkPortfolioDTO(portfolioId);
		return ResponseEntity.ok().body(portfolio);
	}

	@GetMapping("/{id}/lots")
	public ResponseEntity<List<LotDTO>> getLotsByPorfolio(@PathVariable(value = "id") Long portfolioId)
			throws ResourceNotFoundException {
		return ResponseEntity.ok().body(portfolioService.findLotsByPorfolio(portfolioId));
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<List<PortfolioDTO>> getPortfolioByIdUser(@PathVariable(value = "id") Long userId)
			throws ResourceNotFoundException {
		return ResponseEntity.ok().body(portfolioService.findByIdUser(userId));
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@PostMapping("")
	public PortfolioDTO createPortfolio(@Valid @RequestBody PortfolioDTO portfolio) {
		return portfolioService.save(portfolio);
	}

	@Secured({ "ROLE_ADMIN", "ROLE_USER" })
	@PatchMapping("")
	public ResponseEntity<PortfolioDTO> updatePortfolio(@Valid @RequestBody PortfolioDTO portfolio)
			throws ResourceNotFoundException {
	
		PortfolioDTO updatedPortfolio = portfolioService.save(portfolio);
		return ResponseEntity.ok(updatedPortfolio);
	}

	@Secured("ROLE_USER")
	@DeleteMapping("/{id}")
	public Map<String, Boolean> deletePortfolio(@PathVariable(value = "id") Long portfolioId)
			throws ResourceNotFoundException {
		Portfolio portfolio = this.checkPortfolio(portfolioId);

		portfolioRepository.delete(portfolio);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}

	// Portfolio lots grouped by Symbol
	@GetMapping("/watchlist/{id}")
	public ResponseEntity<List<SymbolLotsDTO>> getPortfolioLotsById(@PathVariable(value = "id") Long portfolioId)
			throws ResourceNotFoundException {
		PortfolioDTO portfolio = this.checkPortfolioDTO(portfolioId);
		return ResponseEntity.ok().body(symbolLotsService.findByPortfolio(portfolioId));
	}

	/**
	 * Busca el usuario y si no lo encuentra devuelve error
	 * 
	 * @param portfolioId
	 * @return
	 * @throws ResourceNotFoundException
	 */
	private PortfolioDTO checkPortfolioDTO(Long portfolioId) throws ResourceNotFoundException {
		return portfolioService.findById(portfolioId);
//				.orElseThrow(() -> new ResourceNotFoundException("Portfolio not found for this id :: " + portfolioId));
	}

	private Portfolio checkPortfolio(Long portfolioId) throws ResourceNotFoundException {
		return portfolioRepository.findById(portfolioId)
				.orElseThrow(() -> new ResourceNotFoundException("Portfolio not found for this id :: " + portfolioId));
	}
}