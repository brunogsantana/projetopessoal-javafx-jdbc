package model.services;

import java.util.ArrayList;
import java.util.List;

import model.entities.Conta;

public class ContaService {
	
	public List<Conta> findAll(){
		List<Conta> list= new ArrayList<>();
		list.add(new Conta (1,"Bruno Santana", "02268984583", "Banco do Brasil", null, null, null, null, null, null, null, null));
		return list;
	}
}
