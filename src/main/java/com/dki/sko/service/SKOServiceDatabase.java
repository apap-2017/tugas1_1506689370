package com.dki.sko.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dki.sko.dao.SKOMapper;
import com.dki.sko.model.AddressModel;
import com.dki.sko.model.FamilyModel;
import com.dki.sko.model.KecamatanModel;
import com.dki.sko.model.KelurahanModel;
import com.dki.sko.model.KotaModel;
import com.dki.sko.model.LocationModel;
import com.dki.sko.model.NikModel;
import com.dki.sko.model.NkkModel;
import com.dki.sko.model.ResidentModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SKOServiceDatabase implements SKOService
{
	@Autowired
	private SKOMapper mapper;

	@Override
	public ResidentModel selectResidentByNik(String nik) {
		log.info ("select resident with nik {}", nik);
		return mapper.selectResidentByNik (nik);
	}

	@Override
	public AddressModel selectAddressByKelurahan(int id_kelurahan) {
		log.info ("select address with id_kelurahan {}", id_kelurahan);
		return mapper.selectAddressByKelurahan(id_kelurahan);
	}

	@Override
	public FamilyModel selectFamilyById(int id) {
		log.info ("select family with id {}", id);
		return mapper.selectFamilyById(id);
	}

	@Override
	public FamilyModel selectFamilyByNkk(String nkk) {
		log.info ("select family with nkk {}", nkk);
		return mapper.selectFamilyByNkk(nkk);
	}

	@Override
	public void addResident(ResidentModel resident) {
		log.info ("added new resident with nik {}", resident.getNik());
		mapper.addResident(resident);
	}

	@Override
	public NikModel getLastNik(String nik) {
		log.info ("get last nik with similar pattern with {}", nik);
		return mapper.getLastNik(nik);
	}

	@Override
	public List<LocationModel> selectAllLocation() {
		log.info ("get all location");
		return mapper.getAllLocation();
	}

	@Override
	public List<KecamatanModel> getKecamatanByKota(String kota) {
		log.info ("get all kecamatan from kota with kode {}", kota);
		return mapper.getKecamatanByKota(kota);
	}

	@Override
	public List<KelurahanModel> getKelurahanByKecamatan(String kecamatan) {
		return mapper.getKelurahanByKecamatan(kecamatan);
	}

	@Override
	public List<KotaModel> selectAllKota() {
		return mapper.selectAllKota();
	}

	@Override
	public NkkModel getLastNkk(String nkk) {
		log.info ("get last nkk with similar pattern with {}", nkk);
		return mapper.getLastNkk(nkk);
	}

	@Override
	public void addFamily(FamilyModel family) {
		log.info ("added new family with nkk {}", family.getNomorKk());
		mapper.addFamily(family);
	}

	@Override
	public int getKelurahanIdByKode(String kelurahan) {
		
		return mapper.getKelurahanIdByKode(kelurahan);
	}

	@Override
	public void updateResident(String oldNik, ResidentModel resident) {
		log.info ("updated resident with nik {}", resident.getNik());
		mapper.updateResident(oldNik,resident);
	}

	@Override
	public String getKelurahanById(int idKelurahan) {
		log.info ("get kode kelurahan with id {}", idKelurahan);
		return mapper.getKelurahanById(idKelurahan);
	}

	@Override
	public String getKecamatanByKelurahan(String kelurahan) {
		log.info ("get kecamatan from kelurahan with kode {}", kelurahan);
		return mapper.getKecamatanByKelurahan(kelurahan);
	}

	@Override
	public String getKotaByKecamatan(String kecamatan) {
		log.info ("get kota from kecamatan with kode", kecamatan);
		return mapper.getKotaByKecamatan(kecamatan);
	}

	@Override
	public List<KotaModel> getAllKota() {
		return mapper.getAllKota();
	}

	@Override
	public void updateFamily(String oldNkk, FamilyModel family) {
		log.info ("updated family with nkk {}", family.getNomorKk());
		mapper.updateFamily(oldNkk,family);
	}

	@Override
	public void changeMortalityStatus(String nik) {
		mapper.changeMortalityStatus(nik);
	}

	@Override
	public int countLivingFamilyMembers(String nik) {
		return mapper.countLivingFamilyMembers(nik);
	}
	
	@Override
	public String selectFamilyNkkByMembers(String nik) {
		return mapper.selectFamilyNKKByMembers(nik);
	}

	@Override
	public void changeFamilyStatus(String nik) {
		mapper.changeFamilyStatus(nik);
	}

	@Override
	public List<ResidentModel> getAllResidentByKelurahan(String kelurahan) {
		return mapper.getAllResidentByKelurahan(kelurahan);
	}


}
