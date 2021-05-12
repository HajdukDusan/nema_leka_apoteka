Vue.component("PacijentFarmaceuti", {
  data: function () {
    return {
      message: "",
      apoteka: "",
      cookie: "",
      farmaceuti: [],
      pageSizes: [2, 6, 10, 20, 50],
      fields: [
        {
          key: "username",
          sortable: true,
        },
        {
          key: "prezime",
          sortable: true,
        },
        {
          key: "ime",
          sortable: true,
        },
        {
          key: "prosecnaOcena",
          sortable: true,
        },
        {
          key: "radnoVremePocetak",
          sortable: true,
          label: "Pocetak radnog vremena",
          formatter: (value, key, item) => {
            return moment(value).format("HH:mm");
          },
        },
        {
          key: "radnoVremeKraj",
          sortable: true,
          label: "Kraj radnogVremena",
          formatter: (value, key, item) => {
            return moment(value).format("HH:mm");
          },
        },
      ],
      page: 1,
      count: "",
      pageSize: 6,
      table_is_busy: false,
      sortDesc: false,
      sortBy: "username",
      pretragaIme: "",
      pretragaPrezime: "",
      ocena: 0,
      pocetakRadnog: "00:00",
      krajRadnog: "23:59",
      naziviApoteka: "",
      apoteka: "",
    };
  },
  mounted() {
    this.cookie = localStorage.getItem("cookie");
    this.apotekeNazivi();
  },
  template: `<div>
       <b-navbar toggleable="lg" type="dark" variant="dark">
        <img src="../res/pics/logo.png" alt="Logo">
        <b-navbar-brand href="#">Sistem Apoteka</b-navbar-brand>

        <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

        <b-collapse id="nav-collapse" is-nav>
          <b-navbar-nav>
            <b-nav-item href="#/home-pacijent/">Home</b-nav-item>
            <b-nav-item href="#/home-pacijent/apoteke">Apoteke</b-nav-item>
            <b-nav-item href="#/home-pacijent/lekovi">Lekovi</b-nav-item>
            <b-nav-item href="#/home-pacijent/pregledi">Pregledi</b-nav-item>
            <b-nav-item href="#/pacijent_dermatolozi">Dermatolozi</b-nav-item>
            <b-nav-item href="#/pacijent_farmaceuti">Farmaceuti</b-nav-item>
          </b-navbar-nav>

          <!-- Right aligned nav items -->
          <b-navbar-nav class="ml-auto">
			<b-nav-item href="#/izmena-podataka" right>Profil</b-nav-item>
            <b-nav-item v-on:click="logout" right>Odjavi se</b-nav-item>
          </b-navbar-nav>
        </b-collapse>
      </b-navbar>

      <router-view/>
          <b-card style="margin: 40px auto; max-width: 2000px">
        <b-container>
          <b-row>
            <b-col>
              <b-form-input v-model="pretragaIme" placeholder="Pretrazite po imenu"></b-form-input>
            </b-col>
            <b-col>
              <b-form-input v-model="pretragaPrezime" placeholder="Pretrazite po prezimenu"></b-form-input>
            </b-col>
            <b-col>
              <b-button v-on:click="pretraga" style="float: right">Pretrazi</b-button>
              <b-button v-on:click="obrisiPretragu" style="float: right" variant="danger">Resetuj</b-button>
            </b-col>
            </b-row>
                        <br>
            <b-row>

            <b-col sm="2">
        <label for="range-2">Prosecna ocena:
        <b-form-input id="range-2" v-model="ocena" @change="handleOcenaChange($event)" type="range" size = "sm" min="0" max="5" step="0.5"></b-form-input>
        
        <div class="mt-2">Farmaceuti sa prosecnom ocenom vecom od: {{ ocena }}</div>
        </label>
        </b-col>
        <b-col sm="2">
                       <select v-model="apoteka" @change="handleApotekaChange($event)" style="float: right">
                <option v-for="apoteka1 in naziviApoteka" :key="apoteka1" :value="apoteka1">
                  {{ apoteka1 }}
                </option>
              </select>
        </b-col>
        
        </b-row>
        
        <b-row>
          <b-col><b-form-input type= "time" v-model="pocetakRadnog" @change="handlePocetakChange($event)":max=this.krajRadnog ></b-form-input></b-col>
          <b-col><b-form-input type= "time" v-model="krajRadnog" @change="handleKrajChange($event)" :min=pocetakRadnog></b-form-input></b-col>
        </b-row>
            <br>
		          <b-row>
                    <b-table
                        striped
                        id="farmaceuti-tabela"
                        hover
                        :items="itemProvider"
                        :fields="fields"
                        :per-page="pageSize"
                        :current-page="page"
                        :busy.sync="table_is_busy"
                        :sort-by.sync="sortBy"
                        sort-icon-left
                        responsive="sm"
                        :sort-desc.sync="sortDesc">
         </b-row>
         <b-row>
          <b-col>
            <b-pagination
                  v-model="page"
                  :total-rows="count"
                  :per-page="pageSize"
                  @change="handlePageChange"
              ></b-pagination>
              </b-col>
           <b-col>
              <select v-model="pageSize" @change="handlePageSizeChange($event)" style="float: right">
                <option v-for="size in pageSizes" :key="size" :value="size">
                  {{ size }}
                </option>
              </select>
          </b-col>
         </b-row>
         </b-containter>
      </b-card>
      </div>
      `,
  methods: {
    itemProvider: function (ctx) {
      console.log(ctx);
      return this.retrieveFarmaceute(
        ctx.page,
        ctx.pageSize,
        ctx.sortBy,
        ctx.sortDesc,
        this.pretragaIme,
        this.pretragaPrezime
      );
    },
    retrieveFarmaceute: async function (
      page,
      pageSize,
      sortBy,
      sortDesc,
      pretragaIme,
      pretragaPrezime
    ) {
      this.table_is_busy = true;
      var today = new Date();
      var date =
        today.getFullYear() +
        "-" +
        String(today.getMonth() + 1).padStart(2, "0") +
        "-" +
        String(today.getDate()).padStart(2, "0") +
        "T" +
        String(today.getHours()).padStart(2, "0") +
        ":" +
        String(today.getMinutes()).padStart(2, "0") +
        ":" +
        String(today.getSeconds()).padStart(2, "0") +
        ".000Z";
      var date1 =
        today.getFullYear() +
        "-" +
        String(today.getMonth() + 1).padStart(2, "0") +
        "-" +
        String(today.getDate()).padStart(2, "0") +
        "T" +
        String(today.getHours()).padStart(2, "0") +
        ":" +
        String(today.getMinutes()).padStart(2, "0") +
        ":" +
        String(today.getSeconds()).padStart(2, "0") +
        ".000Z";
      let info = {
        params: {
          cookie: this.cookie,
          page: this.page - 1,
          size: this.pageSize,
          sortBy: sortBy,
          sortDesc: sortDesc,
          pretraziIme: pretragaIme,
          pretraziPrezime: pretragaPrezime,
          ocena: this.ocena,
          pocetak: "2008-01-01T" + this.pocetakRadnog + ":00.000Z",
          kraj: "2008-01-01T" + this.krajRadnog + ":00.000Z",
          apoteka: this.apoteka,
        },
      };
      await axios
        .get("/zdravstveniradnik/getAllFarmaPage", info)
        .then((response) => {
          console.log(response);
          this.farmaceuti = response.data["content"];
          this.count = response.data["totalElements"];
        })
        .catch((e) => {
          console.log(e);
        });
      console.log(this.farmaceuti);
      this.table_is_busy = false;
      return this.farmaceuti;
    },
    logout: function () {
      localStorage.clear();
      app.$router.push("/");
    },
    pretraga: function () {
      this.page = 1;
      this.$root.$emit("bv::refresh::table", "farmaceuti-tabela");
    },
    handlePageChange(value) {
      this.page = value;
    },
    handlePageSizeChange(event) {
      this.page = 1;
      this.items_per_page = event.target.value;
    },
    handleApotekaChange(event) {
      this.page = 1;
      this.apoteka = event.target.value;
      this.$root.$emit("bv::refresh::table", "farmaceuti-tabela");
    },
    handlePageSizeChange(event) {
      this.page = 1;
      this.pageSize = event.target.value;
      this.$root.$emit("bv::refresh::table", "farmaceuti-tabela");
    },
    handleOcenaChange(event) {
      this.page = 1;
      this.ocena = event;
      console.log(this.ocena);
      this.$root.$emit("bv::refresh::table", "farmaceuti-tabela");
    },
    handlePocetakChange(event) {
      this.page = 1;
      this.pocetakRadnog = event;
      this.$root.$emit("bv::refresh::table", "farmaceuti-tabela");
    },
    handleKrajChange(event) {
      this.page = 1;
      this.krajRadnog = event;
      this.$root.$emit("bv::refresh::table", "farmaceuti-tabela");
    },
    obrisiPretragu: function () {
      this.pretragaIme = "";
      this.pretragaPrezime = "";
      this.page = 1;
      this.$root.$emit("bv::refresh::table", "farmaceuti-tabela");
    },
    apotekeNazivi: function () {
      let info = {
        params: {
          cookie: this.cookie,
        },
      };
      axios.get("/apoteke/allApoteke/", info).then((response) => {
        this.naziviApoteka = JSON.parse(JSON.stringify(response.data));
        console.log(this.naziviApoteka);
      });
    },
  },
});
