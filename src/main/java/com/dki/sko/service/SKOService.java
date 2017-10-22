package com.dki.sko.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dki.sko.model.AddressModel;
import com.dki.sko.model.FamilyModel;
import com.dki.sko.model.KecamatanModel;
import com.dki.sko.model.KelurahanModel;
import com.dki.sko.model.KotaModel;
import com.dki.sko.model.LocationModel;
import com.dki.sko.model.NikModel;
import com.dki.sko.model.NkkModel;
import com.dki.sko.model.ResidentModel;

public interface SKOService
{

	ResidentModel selectResidentByNik(String nik);
	AddressModel selectAddressByKelurahan(int id_kelurahan);
	FamilyModel selectFamilyById(int id);
	FamilyModel selectFamilyByNkk(String nkk);
	void addResident(ResidentModel resident);
	NikModel getLastNik(String nik);
	List<LocationModel> selectAllLocation();
	List<KecamatanModel> getKecamatanByKota(String kota);
	List <KelurahanModel> getKelurahanByKecamatan(String kecamatan);
	List<KotaModel> selectAllKota();
	NkkModel getLastNkk(String string);
	void addFamily(FamilyModel family);
	int getKelurahanIdByKode(String kelurahan);
	void updateResident(String oldNik, ResidentModel resident);
	String getKelurahanById(int idKelurahan);
	String getKecamatanByKelurahan(String kelurahan);
	String getKotaByKecamatan(String kecamatan);
	List<KotaModel> getAllKota();
	void updateFamily(String oldNkk, FamilyModel family);
	void changeMortalityStatus(String nik);
	void changeFamilyStatus(String nik);
	int countLivingFamilyMembers(String nik);
	String selectFamilyNkkByMembers(String nik);
	List<ResidentModel> getAllResidentByKelurahan(String kelurahan);


}
