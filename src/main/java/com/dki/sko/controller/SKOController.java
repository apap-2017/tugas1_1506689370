package com.dki.sko.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dki.sko.model.AddressModel;
import com.dki.sko.model.FamilyModel;
import com.dki.sko.model.KecamatanModel;
import com.dki.sko.model.KelurahanModel;
import com.dki.sko.model.KotaModel;
import com.dki.sko.model.LocationModel;
import com.dki.sko.model.NikModel;
import com.dki.sko.model.NkkModel;
import com.dki.sko.model.ResidentModel;
import com.dki.sko.service.SKOService;
import com.dki.sko.service.SKOServiceDatabase;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class SKOController
{
	@Autowired
	SKOService DAO;

	@RequestMapping("/")
	public String index ()
	{
		return "index";
	}
	
	
	@RequestMapping(value = "/penduduk", method = RequestMethod.GET)
    public String viewResident(Model model,@RequestParam(value = "nik", required = true) String nik){
		ResidentModel resident = DAO.selectResidentByNik(nik);
		if (resident != null) {
			FamilyModel family = DAO.selectFamilyById(resident.getIdKeluarga());
			AddressModel address = DAO.selectAddressByKelurahan(family.getIdKelurahan());
			model.addAttribute ("resident",resident);
			model.addAttribute ("family",family);
			model.addAttribute ("address",address);
			return "view-resident";
		} else {
			model.addAttribute ("nik",nik);
			return "not-found";
		}
	}
	
	@RequestMapping(value = "/keluarga", method = RequestMethod.GET)
	public String viewFamily(Model model,@RequestParam(value = "nkk", required = true) String nkk){
		FamilyModel family = DAO.selectFamilyByNkk(nkk);
		if (family != null) {
			AddressModel address = DAO.selectAddressByKelurahan(family.getIdKelurahan());
			model.addAttribute ("family",family);
			model.addAttribute ("address",address);
			return "view-family";
		} else {
			model.addAttribute ("nkk",nkk);
			return "not-found";
		}
	}
	
	@RequestMapping(value = "/penduduk/tambah", method = RequestMethod.GET)
	public String viewResidentRegistration(Model model){
		List<LocationModel> locationList = DAO.selectAllLocation();
		model.addAttribute ("locationList",locationList);
		return "form-add-resident";
	}
	
	@RequestMapping(value = "/penduduk/tambah", method = RequestMethod.POST)
	public String submitResidentRegistration(Model model,
			@RequestParam(value = "tempatLahir", required = true) String tempatLahir,
			@RequestParam(value = "nama", required = true) String nama,
			@RequestParam(value = "tanggalLahir", required = true) String tanggalLahir,
			@RequestParam(value = "jenisKelamin", required = true) boolean jenisKelamin,
			@RequestParam(value = "wni", required = true) boolean wni,
			@RequestParam(value = "idKeluarga", required = true) int idKeluarga,
			@RequestParam(value = "agama", required = true) String agama,
			@RequestParam(value = "pekerjaan", required = true) String pekerjaan,
			@RequestParam(value = "statusPerkawinan", required = true) String statusPerkawinan,
			@RequestParam(value = "statusDalamKeluarga", required = true) String statusDalamKeluarga,
			@RequestParam(value = "golonganDarah", required = true) String golonganDarah,
			@RequestParam(value = "wafat", required = true) boolean wafat
	){

		String[] lokasi = tempatLahir.split("-");
		tempatLahir = lokasi[1];
		int idKecamatan = Integer.parseInt(lokasi[0]);
		int idKota = (idKecamatan - (idKecamatan % 1000)) / 1000;
		idKecamatan %= 100;
		
		String[] tanggal = tanggalLahir.split("-");
		if(jenisKelamin) {	
			int hh = Integer.parseInt(tanggal[2]);
			hh += 40;
			tanggal[2] =  String.format("%02d", hh);
		}
		
		String nik = String.format("%04d", idKota) 
					+String.format("%02d", idKecamatan)
					+tanggal[2] + tanggal[1] + tanggal[0].substring(2);
		
		NikModel latest = DAO.getLastNik(nik+"%");
		if(latest == null){
			nik += "0001";
		}else {
			nik = String.format("%016d",Long.parseLong(latest.getNik()) + 1);
		}
		
		ResidentModel resident = new ResidentModel(0, nik, nama, tempatLahir, tanggalLahir, jenisKelamin, wni, idKeluarga, agama, pekerjaan, statusPerkawinan,statusDalamKeluarga, golonganDarah, wafat);
		DAO.addResident(resident);
		
		model.addAttribute ("success",true);
		model.addAttribute ("nik",nik);
		return "form-add-resident";
	}
	
	@RequestMapping(value = "/keluarga/tambah", method = RequestMethod.GET)
	public String viewFamilyRegistration(Model model){
		List<KotaModel> listKota = DAO.selectAllKota();
		model.addAttribute ("kota",listKota);
		return "form-add-family";
	}
	
	@RequestMapping(value = "/keluarga/tambah", method = RequestMethod.POST)
	public String submitFamilyRegistration(Model model,
			@RequestParam(value = "alamat", required = true) String alamat,
			@RequestParam(value = "rt", required = true) int rt,
			@RequestParam(value = "rw", required = true) int rw,
			@RequestParam(value = "kota", required = true) String kota,
			@RequestParam(value = "kecamatan", required = true) String kecamatan,
			@RequestParam(value = "kelurahan", required = true) String kelurahan
	){
		
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
	
		int temp = Integer.parseInt(kecamatan) % 1000;
		int temp2 = ((Integer.parseInt(kecamatan) - temp)/10) + temp; 
		String nkk = String.format("%06d", temp2) 
					+String.format("%02d", day)
					+String.format("%02d", month)
					+String.format("%02d", year % 100);
		
		NkkModel latest = DAO.getLastNkk(nkk+"%");
		
		if(latest == null){
			nkk += "0001";
		}else {
			nkk = String.format("%016d",Long.parseLong(latest.getNkk()) + 1);
		}
		
		FamilyModel family = new FamilyModel(0, nkk, alamat, rt, rw, DAO.getKelurahanIdByKode(kelurahan), false,null);
		DAO.addFamily(family);
		
		model.addAttribute ("success",true);
		model.addAttribute ("nkk",nkk);
		return "form-add-family";
	}
	
	@RequestMapping(value = "/api/getKecamatan", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<KecamatanModel> getKecamatan(Model model,
			@RequestParam(value = "kota", required = true) String kota
	){
		return DAO.getKecamatanByKota(kota);
	}
	
	@RequestMapping(value = "/api/getKelurahan", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<KelurahanModel> getKelurahan(Model model,
			@RequestParam(value = "kecamatan", required = true) String kecamatan
	){
		return DAO.getKelurahanByKecamatan(kecamatan);
		
	}
	
	@RequestMapping(value = "/penduduk/ubah/{nik}",method = RequestMethod.GET)
	public String viewResidentUpdate(Model model, @PathVariable(value = "nik") String nik){
		ResidentModel resident = DAO.selectResidentByNik(nik);
		if (resident != null) {
			model.addAttribute ("resident", resident);
			List<LocationModel> locationList = DAO.selectAllLocation();
			model.addAttribute ("locationList",locationList);
			return "form-update-resident";
		} else {
			model.addAttribute ("nik", nik);
			return "not-found";
		}
		
	}
	@RequestMapping(value = "/updatePenduduk", method = RequestMethod.POST)
	public String submitResidentUpdate(RedirectAttributes redir,
			@RequestParam(value = "oldNik", required = true) String oldNik,
			@RequestParam(value = "tempatLahir", required = true) String tempatLahir,
			@RequestParam(value = "nama", required = true) String nama,
			@RequestParam(value = "tanggalLahir", required = true) String tanggalLahir,
			@RequestParam(value = "jenisKelamin", required = true) boolean jenisKelamin,
			@RequestParam(value = "wni", required = true) boolean wni,
			@RequestParam(value = "idKeluarga", required = true) int idKeluarga,
			@RequestParam(value = "agama", required = true) String agama,
			@RequestParam(value = "pekerjaan", required = true) String pekerjaan,
			@RequestParam(value = "statusPerkawinan", required = true) String statusPerkawinan,
			@RequestParam(value = "statusDalamKeluarga", required = true) String statusDalamKeluarga,
			@RequestParam(value = "golonganDarah", required = true) String golonganDarah,
			@RequestParam(value = "wafat", required = true) boolean wafat
	){
		String[] lokasi = tempatLahir.split("-");
		tempatLahir = lokasi[1];
		int idKecamatan = Integer.parseInt(lokasi[0]);
		int idKota = (idKecamatan - (idKecamatan % 1000)) / 1000;
		idKecamatan %= 100;
		
		String[] tanggal = tanggalLahir.split("-");
		if(jenisKelamin) {	
			int hh = Integer.parseInt(tanggal[2]);
			hh += 40;
			tanggal[2] =  String.format("%02d", hh);
		}
		
		String nik = String.format("%04d", idKota) 
					+String.format("%02d", idKecamatan)
					+tanggal[2] + tanggal[1] + tanggal[0].substring(2);
		
		NikModel latest = DAO.getLastNik(nik+"%");
		if(latest == null){
			nik += "0001";
		}else {
			nik = String.format("%016d",Long.parseLong(latest.getNik()) + 1);
		}
		
		ResidentModel resident = new ResidentModel(0, nik, nama, tempatLahir, tanggalLahir, jenisKelamin, wni, idKeluarga, agama, pekerjaan, statusPerkawinan,statusDalamKeluarga, golonganDarah, wafat);
		DAO.updateResident(oldNik,resident);
		
		redir.addFlashAttribute("success",true);
		redir.addFlashAttribute("oldNik",oldNik);
		return "redirect:/penduduk/ubah/" + nik;
	}
	
	@RequestMapping(value = "/keluarga/ubah/{nkk}",method = RequestMethod.GET)
	public String viewFamilyUpdate(Model model, @PathVariable(value = "nkk") String nkk){
		FamilyModel family = DAO.selectFamilyByNkk(nkk);
		if (family != null) {
	
			String kelurahan = DAO.getKelurahanById(family.getIdKelurahan());
			String kecamatan = DAO.getKecamatanByKelurahan(kelurahan) ;
			String kota = DAO.getKotaByKecamatan(kecamatan) ;
			List<KotaModel> listKota = DAO.selectAllKota();
			List<KecamatanModel> listKecamatan = DAO.getKecamatanByKota(kota);
			List<KelurahanModel> listKelurahan = DAO.getKelurahanByKecamatan(kecamatan);
			
			model.addAttribute ("kelurahan",kelurahan);
			model.addAttribute ("kecamatan",kecamatan);
			model.addAttribute ("kota",kota);
			model.addAttribute ("listKota",listKota);
			model.addAttribute ("listKecamatan",listKecamatan);
			model.addAttribute ("listKelurahan",listKelurahan);
			model.addAttribute ("family",family);
			
			return "form-update-family";
		} else {	
			model.addAttribute ("nkk", nkk);
			return "not-found";
		}
		
	}
	@RequestMapping(value = "/updateKeluarga", method = RequestMethod.POST)
	public String submitFamilyUpdate(RedirectAttributes redir,
			@RequestParam(value = "oldNkk", required = true) String oldNkk,
			@RequestParam(value = "alamat", required = true) String alamat,
			@RequestParam(value = "rt", required = true) int rt,
			@RequestParam(value = "rw", required = true) int rw,
			@RequestParam(value = "kota", required = true) String kota,
			@RequestParam(value = "kecamatan", required = true) String kecamatan,
			@RequestParam(value = "kelurahan", required = true) String kelurahan
	){
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
	
		int temp = Integer.parseInt(kecamatan) % 1000;
		int temp2 = ((Integer.parseInt(kecamatan) - temp)/10) + temp; 
		String nkk = String.format("%06d", temp2) 
					+String.format("%02d", day)
					+String.format("%02d", month)
					+String.format("%02d", year % 100);
		
		NkkModel latest = DAO.getLastNkk(nkk+"%");
		if(latest == null){
			nkk += "0001";
		}else {
			nkk = String.format("%016d",Long.parseLong(latest.getNkk()) + 1);
		}
		
		FamilyModel family = new FamilyModel(0, nkk, alamat, rt, rw, DAO.getKelurahanIdByKode(kelurahan), false,null);
		DAO.updateFamily(oldNkk,family);
		redir.addFlashAttribute("success",true);
		redir.addFlashAttribute("oldNkk",oldNkk);
		return "redirect:/keluarga/ubah/" + nkk;
	}
	
	@RequestMapping(value = "/penduduk/mati", method = RequestMethod.POST)
	public String residentChangeStatus(RedirectAttributes redir,
		@RequestParam(value = "nik", required = true) String nik){
		
		DAO.changeMortalityStatus(nik);

		if(DAO.countLivingFamilyMembers(nik) == 0) {
			DAO.changeFamilyStatus(DAO.selectFamilyNkkByMembers(nik));
		}

		log.info(Integer.toString(DAO.countLivingFamilyMembers(nik)));
		redir.addFlashAttribute("success",true);
		redir.addFlashAttribute("nik",nik);
		return "redirect:/penduduk?nik=" + nik;
	}
	
	@RequestMapping(value = "/penduduk/cari", method = RequestMethod.GET)
	public String viewAllResident(Model model){
		List<KotaModel> listKota = DAO.selectAllKota();
		model.addAttribute ("kota",listKota);
		return "form-viewall-resident";
	}
	
	@RequestMapping(value = "/penduduk/cari", method = RequestMethod.POST)
	public String searchResidentResult(Model model,
			@RequestParam(value = "kota", required = true) String kota,
			@RequestParam(value = "kecamatan", required = true) String kecamatan,
			@RequestParam(value = "kelurahan", required = true) String kelurahan){
		
		List<ResidentModel> residents = DAO.getAllResidentByKelurahan(kelurahan);

		model.addAttribute ("residents",residents);
		return "viewall-resident";
	}
}


