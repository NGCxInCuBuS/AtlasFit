/* AtlasFit original synthesized UI sounds; no copyrighted samples. */
(function(){
const prefs=()=>{state.atlasNext??={};state.atlasNext.soundPrefs??={enabled:false,volume:30};return state.atlasNext.soundPrefs};
let audio=null;
function play(kind){
const p=prefs(),theme=document.body.dataset.visual;
if(!p.enabled||!theme||!['aincrad','monarch','breathing','leaf'].includes(theme))return;
try{
const Context=window.AudioContext||window.webkitAudioContext;if(!Context)return;
audio??=new Context();if(audio.state==='suspended')audio.resume();
const now=audio.currentTime,volume=Math.min(.5,Math.max(0,Number(p.volume||0)/100*.22));
const notes={
aincrad:kind==='confirm'?[[740,0,.11],[1108,.095,.16]]:[[880,0,.07],[1175,.06,.09]],
monarch:kind==='confirm'?[[196,0,.22],[294,.10,.18]]:[[185,0,.14],[147,.06,.18]],
breathing:kind==='confirm'?[[587,0,.13],[784,.09,.18]]:[[650,0,.08],[490,.065,.13]],
leaf:kind==='confirm'?[[440,0,.10],[660,.085,.13]]:[[360,0,.065],[520,.06,.10]]
}[theme];
for(const [frequency,offset,duration] of notes){
const osc=audio.createOscillator(),gain=audio.createGain(),start=now+offset;
osc.type=theme==='monarch'?'triangle':theme==='leaf'?'square':'sine';
osc.frequency.setValueAtTime(frequency,start);
if(theme==='monarch')osc.frequency.exponentialRampToValueAtTime(frequency*.83,start+duration);
gain.gain.setValueAtTime(.0001,start);gain.gain.exponentialRampToValueAtTime(Math.max(.0002,volume*(theme==='leaf'?.5:1)),start+.012);gain.gain.exponentialRampToValueAtTime(.0001,start+duration);
osc.connect(gain);gain.connect(audio.destination);osc.start(start);osc.stop(start+duration+.01);
}
}catch(e){}
}
document.addEventListener('click',function(event){
const btn=event.target.closest('button');if(!btn)return;
if(btn.closest('nav'))play('tab');
else if(btn.classList.contains('primary'))play('confirm');
},true);
function inject(){
if(tab!=='settings')return;
const view=document.getElementById('view');if(!view||view.querySelector('#atlasSoundPanel'))return;
const p=prefs(),panel=document.createElement('div');panel.className='card';panel.id='atlasSoundPanel';
panel.innerHTML='<h3>🎵 Anime-inspired UI sounds</h3><p class="note">Original synthesized menu sounds for Aincrad HUD, Shadow Monarch, Breathing Arts, and Hidden Leaf. Off by default. No audio clips from the shows.</p><label class="row" style="justify-content:flex-start"><input type="checkbox" id="atlasSoundEnabled" '+(p.enabled?'checked':'')+'> Enable menu sounds</label><div class="fieldrow"><label>Effect volume: <span id="atlasSoundLevel">'+p.volume+'</span>%<input type="range" id="atlasSoundVolume" min="0" max="100" step="5" value="'+p.volume+'"></label></div><button id="atlasSoundPreview">Preview current theme</button>';
view.prepend(panel);
panel.querySelector('#atlasSoundEnabled').addEventListener('change',e=>{p.enabled=e.target.checked;save();if(p.enabled)play('confirm')});
panel.querySelector('#atlasSoundVolume').addEventListener('input',e=>{p.volume=Number(e.target.value);panel.querySelector('#atlasSoundLevel').textContent=p.volume;save()});
panel.querySelector('#atlasSoundPreview').addEventListener('click',()=>play('confirm'));
}
const oldAfter=window.atlasAfterDraw;
window.atlasAfterDraw=function(){oldAfter?.();inject()};
const oldBefore=window.atlasBeforeDraw;
window.atlasBeforeDraw=function(){const handled=oldBefore?.()||false;if(!handled)queueMicrotask(inject);return handled};
if(tab==='settings')inject();
})();