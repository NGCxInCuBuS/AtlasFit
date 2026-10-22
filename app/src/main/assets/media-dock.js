(function(){
function update(){
  if(!window.AtlasMusic||!AtlasMusic.status)return;
  var dock=document.querySelector('.musicdock');
  if(!dock)return;
  if(!dock.querySelector('#trackTitle')){
    var block=document.createElement('div');
    block.style.cssText='display:flex;gap:12px;align-items:center;margin:12px 0';
    block.innerHTML='<img id="trackArt" style="width:68px;height:68px;border-radius:10px;object-fit:cover;display:none" alt="Album cover"><div style="flex:1"><b id="trackTitle">No active track</b><div id="trackArtist" class="note">Start music in another app</div><div id="trackApp" class="note"></div></div>';
    dock.insertBefore(block,dock.children[1]||null);
    var grant=document.createElement('button');
    grant.id='mediaGrant';
    grant.textContent='Enable now-playing information';
    grant.onclick=function(){AtlasMusic.requestAccess()};
    dock.appendChild(grant);
  }
  try{
    var d=JSON.parse(AtlasMusic.status());
    document.getElementById('mediaGrant').style.display=d.permission?'none':'block';
    document.getElementById('trackTitle').textContent=d.title||'No active track';
    document.getElementById('trackArtist').textContent=d.artist||'Start playback in a music app';
    document.getElementById('trackApp').textContent=d.app||'';
    var art=document.getElementById('trackArt');
    if(d.art&&d.art.indexOf('data:image/jpeg;base64,')===0){art.src=d.art;art.style.display='block'}
    else {art.removeAttribute('src');art.style.display='none'}
  }catch(e){}
}
setInterval(update,2000);
update();
})();