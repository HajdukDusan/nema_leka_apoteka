Vue.component("IzmenaDermatolog", {
    data: function () {
        return {
            cookie : "",

        };
    },
    mounted() {
        this.cookie = localStorage.getItem("cookie")
    },
    template: `
        <div>
          <p>Home za dermatologa, tvoj izgenerisani token je {{cookie}}</p>
          <b-col>
            <b-card
                :title="name"
                img-src="https://picsum.photos/600/300/?image=25"
                img-alt="Image"
                img-top
                tag="article"
                style="max-width: 20rem;"
                class="mb-2"
            >
              <b-card-text>
                Some quick example text to build on the card title and make up the
                bulk of the card's content.
              </b-card-text>

              <b-button
                  v-b-tooltip.hover
                  title="Tooltip content"
                  v-b-modal.modalId
                  variant="primary"
              >Go somewhere</b-button
              >
              <b-modal id="modalId" title="BootstrapVue">
                <p class="my-4">Hello from modal!</p>
              </b-modal>
            </b-card>
          </b-col>
        </div>
    `,
    methods: {

    },
});
