package com.conceptdesignarchitect.laporanku.api

import com.conceptdesignarchitect.laporanku.activity_ppspm.b_report_budget.grafik.ModelGrafikResponse
import com.conceptdesignarchitect.laporanku.activity_ppspm.d_history.a_pilih_budget.HistoryResponse
import com.conceptdesignarchitect.laporanku.activity.model.ResponseHistory
import com.conceptdesignarchitect.laporanku.models.*
import okhttp3.MultipartBody
import com.conceptdesignarchitect.laporanku.models.PekMinglalu.ResponseDetailPekerjaanlalu
import com.conceptdesignarchitect.laporanku.models.grafik.DataGrafikDetailItem
import com.conceptdesignarchitect.laporanku.models.grafik.ResponseGrafikdetail
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("pekerjaan")
    fun pilihProyek(
        @Field("id") id: Int
    ): Call<ProyekResponse>

    @FormUrlEncoded
    @POST("minggu")
    fun mingguKe(
        @Field("proyek") proyek: String
    ): Call<MingguResponse>

    @FormUrlEncoded
    @POST("section")
    fun lihatsection(
        @Field("proyek") proyek: String
    ): Call<SectionResponse>

    @FormUrlEncoded
    @POST("subpekerjaan")
    fun lihatsubpekerjaan(
        @Field("section") section:String,
        @Field("proyek") proyek:String
    ):Call <SubpekerjaanResponse>

    @FormUrlEncoded
    @POST("bobotminggu")
    fun lihatbobotminggu(
        @Field("proyek") proyek:String,
        @Field("idminggu") idminggu:String,
        @Field("minggu") minggu:String

    ):Call <BobotmingguResponse>

    @FormUrlEncoded
    @POST("Lihatgrafik")
    fun lihatgrafik(
        @Field("proyek") proyek:String

    ):Call <ModelGrafikResponse>

    @FormUrlEncoded
    @POST("Lihatgrafikdetail")
    fun lihatgrafikdetail(
        @Field("proyek") proyek:String

    ):Call <ResponseGrafikdetail>
    @FormUrlEncoded
    @POST("Historybudgeting")
    fun historybudgeting(
        @Field("proyek") proyek:String

    ):Call <HistoryResponse>

    @FormUrlEncoded
    @POST("detailpekerjaan")
    fun lihatdetailpekerjaan(
        @Field("proyek") proyek: String,
        @Field("section") section: String,
        @Field("idminggu") idminggu: String,
        @Field("pekerjaan") pekerjaan: String
    ): Call<DetailpekerjaanResponse>

    @FormUrlEncoded
    @POST("kirimlaporan")
    fun Kirimlaporan(
        @Field("proyek") proyek: String,
        @Field("idminggu") idminggu: String,
        @Field("ttd") ttd: String
    ): Call<KirimResponse>

    @FormUrlEncoded
    @POST("Kirimuangupdate")
    fun Kirimuang(
        @Field("id") proyek:String,
        @Field("kategori") kategori:String,
        @Field("rincian") rincian:String,
        @Field("uang") nilai:String,
        @Field("surat") surat:String
        ):Call <KirimResponse>

    @FormUrlEncoded
    @POST("editbudget")
    fun EditBudget(
        @Field("id") id:String,
        @Field("tgl") tgl:String,
        @Field("kepada") kepada:String,
        @Field("rincian") rincian:String,
        @Field("nilai") nilai:String,
        @Field("surat") surat:String
        ):Call <KirimResponse>

    @FormUrlEncoded
    @POST("validasippk")
    fun Validasilaporan(
        @Field("proyek") proyek: String,
        @Field("idminggu") idminggu: String,
        @Field("ttd")ttd: String
    ): Call<KirimResponse>

    @FormUrlEncoded
    @POST("cekvalidasi")
    fun Cekvalidasi(
        @Field("id") id: Int,
        @Field("proyek") proyek: String,
        @Field("idminggu") idminggu: String
    ): Call<KirimResponse>

    @FormUrlEncoded
    @POST("ceknotif")
    fun ceknotif(
        @Field("id") id: Int
    ): Call<CeknotifResponse>

    @FormUrlEncoded
    @POST("lihatlaporan")
    fun lihatlaporan(
        @Field("id") id: Int
    ):Call <LihatlaporanResponse>

    @Multipart
    @POST("Kirimuang")
    fun upload(
        @Part filePart: MultipartBody.Part
    ): Call<KirimResponse>

    @FormUrlEncoded
    @POST("Simpandatavolume")
    fun simpanvolume(
        @Field("dataproyek") dataproyek: String,
        @Field("dataketerangan") keterangan: String,
        @Field("idminggu") idminggu: String,
        @Field("datamingguke") mingguke: String,
        @Field("iduraian") iduraian: String,
        @Field("datavolume") volume: String,
        @Field("datavolumeasli") volumeasli: String,
        @Field("databobotasli") bobotasli: String
    ): Call<KirimResponse>

    @FormUrlEncoded
    @POST("Lihatminggulalu")
    fun detailLalu(
        @Field("idkerja") idkerja: String,
        @Field("idminggu") idminggu: String
    ): Call<ResponseDetailPekerjaanlalu>

    @FormUrlEncoded
    @POST("Ubahpassword")
    fun ubahpass(
        @Field("id") idkerja: String,
        @Field("password") idminggu: String
    ): Call<LoginResponse>


    @FormUrlEncoded
    @POST("Ubahprofil")
    fun ubahProfil(
        @Field("id") id: String,
        @Field("nip") nip: String,
        @Field("nama") nama: String,
        @Field("email") email: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("History")
    fun getHistorypengawas(
        @Field("id") id: String
    ): Call<ResponseHistory>

    @FormUrlEncoded
    @POST("uploadttd")
    fun upload_ttd(
        @Field("ttd") ttd: String
    ): Call<KirimResponse>


    @FormUrlEncoded
    @POST("Uploaddokumentasi")
    fun upload_foto(
        @Field("poto") poto: String
    ): Call<KirimResponse>

    @FormUrlEncoded
    @POST("Editfoto")
    fun editfoto(
        @Field("id") idminggu: String,
        @Field("foto") namafoto: String
    ): Call<KirimResponse>

    @FormUrlEncoded
    @POST("Lihatfoto")
    fun lihatdokumentasi(
        @Field("idminggu") idminggu: String,
        @Field("subpekerjaan") sub: String,
        @Field("section") sec: String
    ): Call<ResponseDokumentasi>

    @FormUrlEncoded
    @POST("Simpanfoto")
    fun simpanddokumentasi(
        @Field("idminggu") idminggu: String,
        @Field("subpekerjaan") sub: String,
        @Field("section") sec: String,
        @Field("foto")foto : String
    ): Call<KirimResponse>
}