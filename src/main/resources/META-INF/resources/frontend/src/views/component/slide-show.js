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
        if(def.contentType.includes("video")){
        	
        	 if(med.readyState > 3){
                 med.currentTime = 0;
 				 med.play();
             }else{
 		
 				med.addEventListener("canplay", function() {
 					med.currentTime = 0;
 					med.play();
 				}, true);
             }
        }
    	
    	this.displayerElement.appendChild(med);
    	this.currentDef = def;
    	this.currentMedia = med;
    	this.nextSlide = slide;
    }

    _getNextSlideNumber(number, ssSize){
    	number++;
    	if(number>=ssSize) return 0;
    	return number;
    }

    _setCallBack(){
        
        if(this.currentDef.contentType.includes("video")){
        	// duration could be max slide length
        	// or just end of video
            this.currentMedia.onended = () => this.play();
            this.cancelCallback = () => {
            	this.currentMedia.onended = null;
            	this.currentMedia.pause();
            	// reset the callback handle so
            	// methods don't need to know if 
            	// a callback is connected.
            	this.cancelCallback = ()=>{};
            }
        }
        if(this.currentDef.contentType.includes("image")){
            // duration set by app. TODO static for now.
        	var timer = setTimeout(()=> this.play(), 3000);
        	this.cancelCallback = () => {
        		clearTimeout(timer);
        		// reset the callback handle so
				// methods don't need to know if 
				// a callback is connected.
				this.cancelCallback = ()=>{};
        	}
        } 
    }

    play(){
        this.slideShowState = "play";
        this._showSlide(
    	    this._getNextSlideNumber(this.nextSlide, this.ssds.length));
        this._setCallBack();
    }
    stop(){
    	this.slideShowState = "stop";
    	this.nextSlide = 0;
    	this.cancelCallback();
    	// clearTimeout(this.currentCallBack);
    }

    _continue(){
    	if(this.slideShowState === "play"){
    		this._setCallBack();
    	}
    }

    back(){
    	//this.slideShowState = "back";
    	this.cancelCallback();
    	this._showSlide(
    	    this._getPreviousSlideNumber(this.nextSlide, this.ssds.length));
        this._continue();
    }
    // skip the slide but continue, either stopped,
    // or paused.
    forward(){
    	// this.slideShowState = "forward";
    	this.cancelCallback();
    	this._showSlide(
    	    this._getNextSlideNumber(this.nextSlide, this.ssds.length));
        this._continue();
    }
    pause(){
    	this.slideShowState = "pause";
    	this.cancelCallback();
    }

    constructor(definitions, media, displayerElement){
    	this.ssds = definitions;
    	this.media = media;
    	this.displayerElement = displayerElement;
    	this.nextSlide = 0;
    	this.slideShowState = "stop";
    	this.cancelCallback = ()=>{};
    }

}

class SlideShowView extends PolymerElement {
  static get template() {
    return html`
    <style>
      :host {
        display: block;
        padding: 0;
        margin: 0;
        width: 100%;
        height: 100%;
        position: relative;

      }
      #mediaHolder{
      	height: 100%;
      	width: 100%;
      	display: flex;
        justify-content: center;
      }
      .media-vid {
		max-width: 100%;
		max-height: 100%;
	  }
      .media-img {
	    max-width: 100%;
	    max-height: 100%;
      }
     </style>
      <div id="mediaHolder"></div>
    `;
  }
  
  static get properties() {
	    return {
	      mediaDefinitions: {
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
				    img.classList.add("media-img");
				}else{
                   console.log("Video found " + mediaDef.fileName);
				   var vid = document.createElement('video');
				   vid.src = mediaDef.url;
                   vid.classList.add("media-vid");
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
                case "back" :
                    this.ssManager.back();
                    break;
                case "pause" :
                    this.ssManager.pause();
                    break;
                case "stop" : 
				    this.ssManager.stop();
				    break;
                case "play" :
                    this.ssManager.play();
                    break;
           }
      }

	  // Observer method defined as a class method
	  _activeChanged(newValue, oldValue) {
		console.log('received will' + this.mediaDefinitions);
		this.media = new Map();

		// TODO how can I initialise media semi lazily without
		// using this horrible function? Maybe SS preloads as needed.
		this.getMediaForDefinitions(this.media, this.mediaDefinitions);
	    this.ssManager = new SlideShowManager(this.mediaDefinitions, this.media, this.$.mediaHolder);
        console.log(this.media);

	  }

  static get is() {
    return 'media-ss-display';
  }
}

customElements.define(SlideShowView.is, SlideShowView);
