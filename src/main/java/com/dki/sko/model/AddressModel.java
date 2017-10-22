package com.dki.sko.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressModel {
	private int idKelurahan;
	private int idKecamatan;
	private int idKota;
	private String kelurahan;
	private String kecamatan;
	private String kota;
}
