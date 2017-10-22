package com.dki.sko.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.dki.sko.model.AddressModel;
import com.dki.sko.model.FamilyModel;
import com.dki.sko.model.KecamatanModel;
import com.dki.sko.model.KelurahanModel;
import com.dki.sko.model.KotaModel;
import com.dki.sko.model.LocationModel;
import com.dki.sko.model.NikModel;
import com.dki.sko.model.NkkModel;
import com.dki.sko.model.ResidentModel;

import org.apache.ibatis.annotations.Many;

@Mapper
public interface SKOMapper
{
	@Select("select p.id, p.nik,p.nama,p.tempat_lahir,p.tanggal_lahir, if(p.jenis_kelamin='1','true','false') as jenis_kelamin,if(p.is_wni='1','true','false') as is_wni, p.id_keluarga, p.agama, p.pekerjaan, p.status_perkawinan, p.status_dalam_keluarga, p.golongan_darah, if(p.is_wafat='1','true','false') as is_wafat from penduduk p where p.nik = #{nik}")
	@Results(value= {
			@Result(property="id",column="id"),
			@Result(property="nik",column="nik"),
			@Result(property="nama",column="nama"),
			@Result(property="tempatLahir",column="tempat_lahir"),
			@Result(property="tanggalLahir",column="tanggal_lahir"),
			@Result(property="jenisKelamin",column="jenis_kelamin"),
			@Result(property="wni",column="is_wni"),
			@Result(property="idKeluarga",column="id_keluarga"),
			@Result(property="agama",column="agama"),
			@Result(property="pekerjaan",column="pekerjaan"),
			@Result(property="statusPerkawinan",column="status_perkawinan"),
			@Result(property="golonganDarah",column="golongan_darah"),
			@Result(property="statusDalamKeluarga",column="status_dalam_keluarga"),
			@Result(property="wafat",column="is_wafat")	
	})
	ResidentModel selectResidentByNik (@Param("nik") String nik);

	
	@Select("select p.id, p.nik,p.nama,p.tempat_lahir,p.tanggal_lahir, if(p.jenis_kelamin='1','true','false') as jenis_kelamin,if(p.is_wni='1','true','false') as is_wni, p.id_keluarga, p.agama, p.pekerjaan, p.status_perkawinan, p.status_dalam_keluarga, p.golongan_darah, if(p.is_wafat='1','true','false') as is_wafat from penduduk p where p.id_keluarga = #{id}")
	@Results(value= {
			@Result(property="id",column="id"),
			@Result(property="nik",column="nik"),
			@Result(property="nama",column="nama"),
			@Result(property="tempatLahir",column="tempat_lahir"),
			@Result(property="tanggalLahir",column="tanggal_lahir"),
			@Result(property="jenisKelamin",column="jenis_kelamin"),
			@Result(property="wni",column="is_wni"),
			@Result(property="idKeluarga",column="id_keluarga"),
			@Result(property="agama",column="agama"),
			@Result(property="pekerjaan",column="pekerjaan"),
			@Result(property="statusPerkawinan",column="status_perkawinan"),
			@Result(property="golonganDarah",column="golongan_darah"),
			@Result(property="statusDalamKeluarga",column="status_dalam_keluarga"),
			@Result(property="wafat",column="is_wafat")	
	})
	ResidentModel selectResidentByFamilyId(@Param("id") int id);
	
	@Select("select l.id as id_kelurahan, c.id as id_kecamatan, t.id as id_kota, l.nama_kelurahan, c.nama_kecamatan, t.nama_kota from kelurahan l, kecamatan c, kota t where l.id = #{id_kelurahan} and l.id_kecamatan = c.id and c.id_kota = t.id")
	@Results(value= {
			@Result(property="idKelurahan",column="id_kelurahan"),
			@Result(property="idKecamatan",column="id_kecamatan"),
			@Result(property="idKota",column="id_kota"),
			@Result(property="kelurahan",column="nama_kelurahan"),
			@Result(property="kecamatan",column="nama_kecamatan"),
			@Result(property="kota",column="nama_kota")	
	})
	AddressModel selectAddressByKelurahan(@Param("id_kelurahan") int id_kelurahan);

	
	@Select("select id, nomor_kk, alamat, rt, rw, id_kelurahan, is_tidak_berlaku from keluarga where id = #{id}")
	@Results(value= {
			@Result(property="id",column="id"),
			@Result(property="nomorKk",column="nomor_kk"),
			@Result(property="alamat",column="alamat"),
			@Result(property="rt",column="rt"),
			@Result(property="rw",column="rw"),
			@Result(property="idKelurahan",column="id_kelurahan"),
			@Result(property="isTidakBerlaku",column="is_tidak_berlaku")
	})
	FamilyModel selectFamilyById(@Param("id") int id);

	
	@Select("select id, nomor_kk, alamat, rt, rw, id_kelurahan, is_tidak_berlaku from keluarga where nomor_kk = #{nkk}")
	@Results(value= {
			@Result(property="id",column="id"),
			@Result(property="nomorKk",column="nomor_kk"),
			@Result(property="alamat",column="alamat"),
			@Result(property="rt",column="rt"),
			@Result(property="rw",column="rw"),
			@Result(property="idKelurahan",column="id_kelurahan"),
			@Result(property="isTidakBerlaku",column="is_tidak_berlaku"),
			@Result(property="members",column="id", javaType=List.class, many=@Many(select="selectResidentByFamilyId"))
	})
	FamilyModel selectFamilyByNkk(@Param("nkk") String nkk);
	
	@Insert("INSERT INTO penduduk (nik, nama, tempat_lahir, tanggal_lahir, jenis_kelamin, is_wni, id_keluarga, agama, pekerjaan, status_perkawinan, status_dalam_keluarga, golongan_darah, is_wafat) VALUES (#{nik}, #{nama}, #{tempatLahir}, #{tanggalLahir}, #{jenisKelamin}, #{wni}, #{idKeluarga},#{agama}, #{pekerjaan}, #{statusPerkawinan}, #{statusDalamKeluarga}, #{golonganDarah}, #{wafat})")
	void addResident(ResidentModel resident);

	@Select("select nik from penduduk where nik like #{nik} order by nik desc limit 1")
	@Results(value= {
			@Result(property="nik",column="nik")
	})
	NikModel getLastNik(@Param("nik") String nik);
	
	@Select("select kode_kecamatan, nama_kecamatan from kecamatan")
	@Results(value= {
			@Result(property="kodeKecamatan",column="kode_kecamatan"),
			@Result(property="kecamatan",column="nama_kecamatan")
	})
	List<LocationModel> getAllLocation();

	@Select("select kecamatan.kode_kecamatan, kecamatan.nama_kecamatan from kota,kecamatan where kota.kode_kota = #{kota} and kota.id = kecamatan.id_kota")
	@Results(value= {
			@Result(property="kode",column="kode_kecamatan"),
			@Result(property="nama",column="nama_kecamatan")
	})
	List<KecamatanModel> getKecamatanByKota(@Param("kota") String kota);

	@Select("select kelurahan.kode_kelurahan, kelurahan.nama_kelurahan from kecamatan,kelurahan where kecamatan.kode_kecamatan = #{kecamatan} and kecamatan.id = kelurahan.id_kecamatan")
	@Results(value= {
			@Result(property="kode",column="kode_kelurahan"),
			@Result(property="nama",column="nama_kelurahan")
	})
	List<KelurahanModel> getKelurahanByKecamatan(@Param("kecamatan") String kecamatan);

	@Select("select kode_kota, nama_kota from kota")
	@Results(value= {
			@Result(property="kode",column="kode_kota"),
			@Result(property="nama",column="nama_kota")
	})
	List<KotaModel> selectAllKota();

	@Select("select nomor_kk from keluarga where nomor_kk like #{nkk} order by nomor_kk desc limit 1")
	@Results(value= {
			@Result(property="nkk",column="nomor_kk")
	})
	NkkModel getLastNkk(String nkk);

	@Insert("INSERT INTO keluarga (nomor_kk, alamat, rt, rw, id_kelurahan, is_tidak_berlaku ) VALUES (#{nomorKk}, #{alamat}, #{rt}, #{rw}, #{idKelurahan}, false)")
	void addFamily(FamilyModel family);

	@Select("select id from kelurahan where kode_kelurahan = #{kelurahan}")
	int getKelurahanIdByKode(@Param("kelurahan")String kelurahan);

	@Update("UPDATE penduduk SET nik = #{resident.nik}, nama = #{resident.nama}, tempat_lahir = #{resident.tempatLahir}, tanggal_lahir = #{resident.tanggalLahir}, jenis_kelamin = #{resident.jenisKelamin}, is_wni = #{resident.wni}, id_keluarga = #{resident.idKeluarga}, agama = #{resident.agama}, pekerjaan = #{resident.pekerjaan}, status_perkawinan = #{resident.statusPerkawinan}, status_dalam_keluarga = #{resident.statusDalamKeluarga}, golongan_darah = #{resident.golonganDarah}, is_wafat = #{resident.wafat} WHERE nik = #{oldNik}")
	void updateResident(@Param("oldNik") String oldNik,@Param("resident") ResidentModel resident);

	@Select("select kode_kelurahan from kelurahan where id = #{idKelurahan}")
	String getKelurahanById(@Param("idKelurahan") int idKelurahan);

	@Select("select kode_kecamatan from kecamatan, kelurahan where kelurahan.kode_kelurahan = #{kelurahan} and kelurahan.id_kecamatan = kecamatan.id")
	String getKecamatanByKelurahan(@Param("kelurahan")String kelurahan);

	@Select("select kode_kota from kota, kecamatan where kecamatan.kode_kecamatan = #{kecamatan} and kecamatan.id_kota = kota.id")
	String getKotaByKecamatan(@Param("kecamatan") String kecamatan);

	@Select("select kode_kota, nama_kota from kota")
	@Results(value= {
			@Result(property="kode",column="kode_kota"),
			@Result(property="nama",column="nama_kota")
	})
	List<KotaModel> getAllKota();

	@Update("UPDATE keluarga SET nomor_kk = #{family.nomorKk}, alamat = #{family.alamat}, rt = #{family.rt}, rw = #{family.rw}, id_kelurahan = #{family.idKelurahan} WHERE nomor_kk = #{oldNkk}")
	void updateFamily(@Param("oldNkk")String oldNkk,@Param("family") FamilyModel family);

	@Update("UPDATE penduduk SET is_wafat= true where nik = #{nik}")
	void changeMortalityStatus(@Param("nik") String nik);

	@Select("select count(penduduk.is_wafat) from penduduk, keluarga where keluarga.nomor_kk = #{nkk} and keluarga.id = penduduk.id_keluarga and penduduk.is_wafat=false")
	Integer countLivingFamilyMembers(@Param("nkk")  String nkk);

	@Select("select nomor_kk from keluarga, penduduk where penduduk.nik = #{nik} and keluarga.id = penduduk.id_keluarga")
	String selectFamilyNKKByMembers(@Param("nik")  String nik);
	
	@Update("Update keluarga set is_tidak_berlaku = true where nomor_kk = #{nkk} ")
	void changeFamilyStatus(@Param("nkk") String nkk);

	@Select("select p.id, p.nik,p.nama,p.tempat_lahir,p.tanggal_lahir, if(p.jenis_kelamin='1','true','false') as jenis_kelamin,if(p.is_wni='1','true','false') as is_wni, p.id_keluarga, p.agama, p.pekerjaan, p.status_perkawinan, p.status_dalam_keluarga, p.golongan_darah, if(p.is_wafat='1','true','false') as is_wafat from penduduk p, keluarga, kelurahan where kelurahan.kode_kelurahan= #{kelurahan} and keluarga.id_kelurahan = kelurahan.id and keluarga.id = p.id_keluarga")
	@Results(value= {
			@Result(property="id",column="id"),
			@Result(property="nik",column="nik"),
			@Result(property="nama",column="nama"),
			@Result(property="tempatLahir",column="tempat_lahir"),
			@Result(property="tanggalLahir",column="tanggal_lahir"),
			@Result(property="jenisKelamin",column="jenis_kelamin"),
			@Result(property="wni",column="is_wni"),
			@Result(property="idKeluarga",column="id_keluarga"),
			@Result(property="agama",column="agama"),
			@Result(property="pekerjaan",column="pekerjaan"),
			@Result(property="statusPerkawinan",column="status_perkawinan"),
			@Result(property="golonganDarah",column="golongan_darah"),
			@Result(property="statusDalamKeluarga",column="status_dalam_keluarga"),
			@Result(property="wafat",column="is_wafat")	
	})
	List<ResidentModel> getAllResidentByKelurahan(String kelurahan);
}
