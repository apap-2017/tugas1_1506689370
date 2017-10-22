package com.dki.sko.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResidentModel
{
	private int id;
	private String nik;
	private String nama;
	private String tempatLahir;
	private String tanggalLahir;
	private boolean jenisKelamin;
	private boolean wni;
	private int idKeluarga;
	private String agama;
	private String pekerjaan;
	private String statusPerkawinan;
	private String statusDalamKeluarga;
	private String golonganDarah;
	private boolean wafat;
	

}
