/* AtlasFit offline machine photo importer and gym-specific variants. */
(function(){
const oldPict=pict,oldCredit=photoCredit,oldAfter=window.atlasAfterDraw;
const entries=[
['Treadmill','Treadmill'],['Abdominal crunch','Abdominal Crunch — Yellow'],['Abdominal crunch purple','Abdominal Crunch — Purple'],['Seated row','Seated Row'],['Leg extension','Leg Extension'],['Biceps curl','Biceps Curl'],['Triceps extension','Triceps Extension'],['Seated leg curl','Leg Curl'],['Shoulder press','Shoulder Press'],['Lat pulldown','Lat Pulldown'],['Leg press','Leg Press']
];
function store(){state.atlasNext??={};state.atlasNext.machinePhotos??={};return state.atlasNext.machinePhotos}
function photoKey(name){return name==='Abdominal crunch'?(state.atlasNext?.preferredCrunchPhoto||'Abdominal crunch'):name}
pict=function(name,kind){return store()[photoKey(name)]||oldPict(name,kind)};
photoCredit=function(name){return store()[photoKey(name)]?'<span class="note">📷 Your gym photo · stored offline on this device</span>':oldCredit(name)};
window.atlasImportMachinePhoto=function(key,input){
const file=input.files?.[0];if(!file)return;
if(!file.type.startsWith('image/')){showToast('Choose a photo');return}
const reader=new FileReader();reader.onload=()=>{
const image=new Image();image.onload=()=>{
const canvas=document.createElement('canvas'),max=850,scale=Math.min(1,max/Math.max(image.width,image.height));
canvas.width=Math.round(image.width*scale);canvas.height=Math.round(image.height*scale);
canvas.getContext('2d').drawImage(image,0,0,canvas.width,canvas.height);
store()[key]=canvas.toDataURL('image/jpeg',.66);save();draw();showToast('Saved offline photo: '+key)
};image.src=reader.result
};reader.readAsDataURL(file)
};
window.atlasRemoveMachinePhoto=function(key){delete store()[key];save();draw();showToast('Photo removed')};
window.atlasChooseCrunchPhoto=function(key){state.atlasNext.preferredCrunchPhoto=key;save();draw()};
function panel(){
if(tab!=='library')return;
const v=document.getElementById('view');if(!v||v.querySelector('#atlasLocalPhotos'))return;
const el=document.createElement('section');el.className='card';el.id='atlasLocalPhotos';
el.innerHTML='<h3>📷 Your Planet Fitness machines · offline photos</h3><p class="note">Add your real machine photos once. AtlasFit stores compressed copies on your phone and uses them in both Machines and Workout. These photos are included in your Atlas Vault JSON backup.</p><div class="atlas-photo-grid">'+entries.map(([key,title])=>'<div class="atlas-photo-slot"><div class="atlas-photo-title">'+title+'</div>'+(store()[key]?'<img src="'+store()[key]+'" alt="'+title+' photo"><button onclick="atlasRemoveMachinePhoto(\''+key+'\')">Remove photo</button>':'<div class="atlas-photo-empty">No photo yet</div>')+'<label class="atlas-photo-upload">Choose photo<input type="file" accept="image/*" onchange="atlasImportMachinePhoto(\''+key+'\',this)"></label></div>').join('')+'</div><p class="note">Abdominal Crunch display photo:</p><div class="row"><button onclick="atlasChooseCrunchPhoto(\'Abdominal crunch\')">Yellow machine</button><button onclick="atlasChooseCrunchPhoto(\'Abdominal crunch purple\')">Purple machine</button></div>';
v.prepend(el);
}
const style=document.createElement('style');style.textContent='.atlas-photo-grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(135px,1fr));gap:10px}.atlas-photo-slot{border:1px solid var(--stroke);border-radius:12px;padding:10px;display:flex;flex-direction:column;gap:8px}.atlas-photo-title{font-weight:700;font-size:12px;min-height:28px}.atlas-photo-slot img{width:100%;height:115px;object-fit:cover;border-radius:8px}.atlas-photo-empty{height:115px;display:grid;place-items:center;color:var(--muted);background:var(--bg);border-radius:8px;font-size:12px}.atlas-photo-upload{display:block;background:var(--accent);color:#071f25;text-align:center;padding:9px;border-radius:9px;font-weight:700;font-size:12px;cursor:pointer}.atlas-photo-upload input{position:absolute;width:1px;height:1px;opacity:0;pointer-events:none}';document.head.appendChild(style);
window.atlasAfterDraw=function(){oldAfter?.();panel()};
})();