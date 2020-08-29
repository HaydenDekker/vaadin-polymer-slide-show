import { PolymerElement } from '@polymer/polymer/polymer-element.js';
import { html } from '@polymer/polymer/lib/utils/html-tag.js';

class SlideShowManager{

//     var displayerElement;
//     var mediaGetter;
//     var slideShowState;
//     var slideShowSlideDefinitions;

//     var nextSlideCallback;

    _getPreviousSlideNumber(number, ssSize){
        number--;
        if(number<0) return ssSize + number;
        return number;
    }

    // need to get a slide, 
    // if none are available...
    // stay on current slide,
    // or do we try and get another slide?
    // Optimisation for later.
    _showSlide(slide){
    	if(typeof this.currentMedia!=='undefined'){
            this.displayerElement.removeChild(this.currentMedia);
    	}
    	var def = this.ssds[slide];
    	var med = this.media.get(def);
    	this.displayerElement.appendChild(med);
    	this.currentMedia = med;
    	this.nextSlide = slide;
    }

    _getNextSlideNumber(number, ssSize){
    	number++;
    	if(number>=ssSize) return 0;
    	return number;
    }

    play(){
        this.slideShowState = "play";
    }
    stop(){
    	this.slideShowState = "stop";
    }

    back(){
    	this.slideShowState = "back";
    }
    forward(){
    	this.slideShowState = "forward";
    	this._showSlide(
    	    this._getNextSlideNumber(this.nextSlide, this.ssds.length));
    }
    pause(){
    	this.slideShowState = "stop";
    }

    constructor(definitions, media, displayerElement){
    	this.ssds = definitions;
    	this.media = media;
    	this.displayerElement = displayerElement;
    	this.nextSlide = 0;
    	this.slideShowState = "stop";
    }

}

class SlideShowView extends PolymerElement {
  static get template() {
    return html`
    <style>
      :host {
        display: block;
        padding: 1em;
      }
     </style>
      <vaadin-text-field id="name"
        label="Your name"
      ></vaadin-text-field>
      <div id="mediaHolder">Say hello</div>
      </vaadin-notification>
    `;
  }
  
  static get properties() {
	    return {
	      imageURLs: {
	        type: Array,
	        // Observer method identified by name
	        observer: '_activeChanged'
	      },
	      mDC: {
            type: Object,
            observer: '_mDCChanged'

	      }
	    }
	  }

      async getMediaForDefinitions(mediaDefImgMap, mediaDefs){
        
	    mediaDefs.forEach(
	        mediaDef=>{
				if(mediaDef.contentType.includes("image")){
				    var img = new Image();
				    img.onload = () => mediaDefImgMap.set(mediaDef, img);
				    img.src = mediaDef.url;
				}else{
                   console.log("Video found " + mediaDef.fileName);
				   var vid = document.createElement('video');
				   vid.src = mediaDef.url;
//vid.autoplay = true;
				   mediaDefImgMap.set(mediaDef, vid); 
				}
        });
      }

      _mDCChanged(newValue, oldValue){
           console.log('mdcChangesd');
           switch(newValue.ssc) {
                case "forward" :
                 this.ssManager.forward();
                 break;

           }
      	  
      }

	  // Observer method defined as a class method
	  _activeChanged(newValue, oldValue) {
		console.log('received will' + this.imageURLs);
		this.media = new Map();

		// TODO how can I initialise media semi lazily without
		// using this horrible function? Maybe SS preloads as needed.
		this.getMediaForDefinitions(this.media, this.imageURLs);
	    this.ssManager = new SlideShowManager(this.imageURLs, this.media, this.$.mediaHolder);
        console.log(this.media);
	    // var images = this.imageURLs.map(media => {var img = new Image(); img.src = media.url; return img;});
	    // this.$.mediaHolder.appendChild(images[0]);
	  }

  static get is() {
    return 'media-ss-display';
  }
}

customElements.define(SlideShowView.is, SlideShowView);
