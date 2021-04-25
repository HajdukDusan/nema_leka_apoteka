Vue.component("CalendarView", {
    data: function () {
        return {
            cookie: "",
            rola: "",
            invalidCookie: false,
            calendar: null,
            selectedEvent: {
                title: null,
                start: new Date(),
                end: new Date(),
                apoteka: {naziv: null, adresa: null},
                pacijent: {ime: null, prezime: null},
                preporuceniLekovi: [],
                dijagnoza: null,
                pregledZakazan: null,
                pregledObavljen: null,
                savetovanjeObavljeno: null,
                trajanje: null,
                eventType: null
            }
        }
    },

    mounted() {
        this.cookie = localStorage.getItem("cookie")
        this.rola = localStorage.getItem("userRole")
        let calendarEl = document.getElementById('calendar');
        let that = this
        let calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            initialDate: new Date(),
            customButtons: {
                homeButton: {
                    text: 'home',
                    click: function () {
                        if (that.rola == "FARMACEUT") app.$router.push("/home-farmaceut")
                        else if (that.rola == "DERMATOLOG") app.$router.push("/home-dermatolog")
                        else {
                            localStorage.clear()
                            app.$router.push("home")
                        }
                    }
                }
            },
            headerToolbar: {
                left: 'homeButton prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay'
            },
            themeSystem: 'bootstrap4',
            events: that.loadData,
            nowIndicator: true,
            expandRows: true,
            slotMinTime: '08:00',
            slotMaxTime: '20:00',
            navLinks: true,
            dayMaxEvents: true,
            eventClick: that.eventSelected,
            locale: 'sr-latn',
            buttonText: {
                today:    'danas',
                month:    'mesec',
                week:     'nedelja',
                day:      'dan',
                list:     'lista'
            },
            weekText: 'sed',
            allDayText: 'ceo dan',
            moreLinkText: function(n) {
                return '+ još ' + n
            },
            noEventsText: 'nеma događaja za prikaz',
            firstDay: 1,
            week: {
                dow: 1, // Monday is the first day of the week.
                doy: 7, // The week that contains Jan 1st is the first week of the year.
            },
        });
        calendar.render();
        this.calendar = calendar
    },
    template:
        `
          <div>
          <link rel="stylesheet" href="css/dermatolog-farmaceut/calendar_view.css" type="text/css">
          <b-alert style="text-align: center;" v-model="invalidCookie" variant="danger">Nisi ulogovan kao
            dermatolog/farmaceut!
          </b-alert>
          <b-modal id="eventModal" title="Detalji">
            <b-container>
              <b-row>
                <b-col>Pocetak:</b-col>
                <b-col>{{ moment(String(selectedEvent.start)).format("DD/MM/YYYY HH:mm") }}</b-col>
              </b-row>
              <b-row>
                <b-col>Kraj:</b-col>
                <b-col>{{ moment(String(selectedEvent.end)).format("DD/MM/YYYY HH:mm") }}</b-col>
              </b-row>
              <b-row v-if=" selectedEvent.pregledObavljen">
                <b-col>Trajanje:</b-col>
                <b-col>{{ moment(String(new Date(selectedEvent.trajanje))).format("mm") }} min</b-col>
              </b-row>
              <b-row v-if="(selectedEvent.pregledZakazan)">
                <b-col>Ime pacijenta:</b-col>
                <b-col>{{ selectedEvent.pacijent.ime }}</b-col>
              </b-row>
              <b-row v-if="selectedEvent.pregledZakazan">
                <b-col>Prezime pacijenta:</b-col>
                <b-col>{{ selectedEvent.pacijent.prezime }}</b-col>
              </b-row>
              <b-row>
                <b-col>Ime Apoteke:</b-col>
                <b-col>{{ selectedEvent.apoteka.naziv }}</b-col>
              </b-row>
              <b-row>
                <b-col>Adresa apoteke:</b-col>
                <b-col>{{ selectedEvent.apoteka.adresa }}</b-col>
              </b-row>
              <b-row v-if="selectedEvent.pregledObavljen">
                <b-col>Dijagnoza:</b-col>
                <b-col>{{ selectedEvent.dijagnoza }}</b-col>
              </b-row>
              <b-row v-if="selectedEvent.pregledObavljen">
                <b-col>Preporuceni lekovi:</b-col>
                <b-col>
                  <b-row v-for="lek in selectedEvent.preporuceniLekovi">- {{ lek.naziv }}</b-row>
                </b-col>
              </b-row>
              <b-row v-if="!selectedEvent.pregledZakazan">
                <br>
                <p>Ovo je dodeljen termin od strane apoteke. Mozete zakazati pregled u ovom terminu.</p>
              </b-row>
            </b-container>
            <template #modal-footer="{ ok }">
              <b-button variant="success" @click="ok()">
                <template
                    v-if="(selectedEvent.pregledObavljen || !selectedEvent.pregledZakazan)">
                  OK
                </template>
                <template
                    v-if="selectedEvent.pregledZakazan && !selectedEvent.pregledObavljen">
                  Zapocni pregled
                </template>
                <template
                    v-if="rola=='FARMACEUT' && !selectedEvent.pregledObavljen">
                  Zapocni savetovanje
                </template>
              </b-button>
            </template>
          </b-modal>
          <b-card id="outline">
            <div id="calendar"/>
          </b-card>
          </div>

        `
    ,
    methods: {
        loadData: async function (fetchInfo, successCallback, failureCallback) {
            let retVal = []
            await axios
                .get("pregledi/getPreglediByZdravstveniRadnik", {
                    params: {
                        "cookie": this.cookie,
                        "start": fetchInfo.start,
                        "end": fetchInfo.end
                    }
                })
                .then(response => {
                    let events = response.data
                    let rola = this.rola
                    for (let event of events) {
                        event.eventType = "PREGLED"
                        retVal.push({
                            title: (() => {
                                if (event.pregledZakazan) {
                                    if (rola == "FARMACEUT")
                                        return "savetovanje"
                                    if (rola == "DERMATOLOG") {
                                        return "pregled"
                                    }
                                } else {
                                    return "dodeljeni termin"
                                }
                            })(),
                            start: new Date(event.start),
                            end: new Date(event.end),
                            event: event,
                            color: (() => {
                                if (new Date() > new Date(event.end)) {
                                    if (event.pregledObavljen)
                                        return "gray"
                                    else
                                        return "red"
                                } else {
                                    if (!event.pregledZakazan)
                                        return "green"
                                }
                            })()
                        })
                    }
                })
                .catch(error => {
                    if (error.request.status == 404) {
                        this.invalidCookie = true
                    }
                })
            await axios
                .get("zdravstveniradnik/fetchOdsustvaInDateRange", {
                    params: {
                        "cookie": this.cookie,
                        "start": fetchInfo.start,
                        "end": fetchInfo.end
                    }
                })
                .then(response => {
                    let events = response.data
                    for (let event of events) {
                        event.eventType = "ODMOR"
                        retVal.push({
                            title: "odmor",
                            start: new Date(event.pocetak),
                            end: new Date(event.kraj),
                            event: event,
                            color: (() => {
                                if (new Date() > new Date(event.end)) {
                                    return "gray"
                                }
                            })()
                        })
                    }
                })
            return retVal
        },
        eventSelected: function (info) {
            let event = info.event.extendedProps.event
            this.selectedEvent.eventType = event.eventType
            if (event.eventType == "PREGLED") {
                this.selectedEvent.title = event.pacijent.ime + " " + event.pacijent.prezime
                this.selectedEvent.start = event.start
                this.selectedEvent.end = event.end
                this.selectedEvent.apoteka = event.apoteka
                this.selectedEvent.pacijent = event.pacijent
                this.selectedEvent.preporuceniLekovi = event.preporuceniLekovi
                this.selectedEvent.dijagnoza = event.dijagnoza
                this.selectedEvent.pregledZakazan = event.pregledZakazan
                this.selectedEvent.pregledObavljen = event.pregledObavljen
                this.selectedEvent.trajanje = event.trajanje
                this.$bvModal.show('eventModal')
            }
        }
    }
});