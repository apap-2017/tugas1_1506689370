package com.dki.sko.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FamilyModel {
	private int id;
	private String nomorKk;
	private String alamat;
	private int rt;
	private int rw;
	private int idKelurahan;
	private boolean isTidakBerlaku;
	private List<ResidentModel> members;
}
