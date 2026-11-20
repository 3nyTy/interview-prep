(function(){
  var PREFIX = '/interview/problems/company/reddit/';
  var seen = {}, queue = [PREFIX + 'tennis-match-scoring'], recs = [], gridFound = 0;

  function txt(el){
    var out = '';
    (function walk(n){
      for (var i = 0; i < n.childNodes.length; i++){
        var c = n.childNodes[i];
        if (c.nodeType === 3) { out += c.nodeValue; continue; }
        if (c.nodeType !== 1) continue;
        var tg = c.tagName;
        if (tg === 'SCRIPT' || tg === 'STYLE' || tg === 'SVG' || tg === 'NOSCRIPT') continue;
        var block = /^(DIV|P|H1|H2|H3|H4|H5|H6|LI|UL|OL|TR|BR|SECTION|HEADER|FOOTER|PRE)$/.test(tg);
        if (block && out && out.charAt(out.length - 1) !== '\n') out += '\n';
        walk(c);
        if (block && out.charAt(out.length - 1) !== '\n') out += '\n';
      }
    })(el);
    return out.replace(/\n{3,}/g, '\n\n').replace(/[ \t]+\n/g, '\n').trim();
  }

  while (queue.length && recs.length < 80) {
    var u = queue.shift();
    if (seen[u]) continue;
    seen[u] = 1;
    var rec = { url: 'https://www.1point3acres.com' + u };
    try {
      var x = new XMLHttpRequest();
      x.open('GET', u, false);
      x.send();
      var doc = new DOMParser().parseFromString(x.responseText, 'text/html');
      var nd = doc.getElementById('__NEXT_DATA__');
      var pp = nd ? (JSON.parse(nd.textContent).props.pageProps || {}) : {};
      rec.slug = pp.slug || u.slice(PREFIX.length);
      rec.questionTitle = pp.questionTitle || null;
      rec.category = pp.category || null;
      rec.tags = pp.tags || [];
      rec.roles = pp.roles || [];
      rec.sourceCount = (pp.sourceCount === undefined ? null : pp.sourceCount);
      rec.isLocked = (pp.isLocked === undefined ? null : pp.isLocked);
      rec.overviewLen = (pp.overview || '').length;
      rec.overviewTeaser = pp.overview || null;
      rec.answerLen = (pp.answerText || '').length;
      rec.hasFullStatement = !pp.isLocked && (pp.overview || '').length > 300;

      var h1 = doc.querySelector('h1');
      var main = h1 ? h1.closest('main') : null;
      var grid = main ? main.querySelector('div.grid.grid-cols-3') : null;
      if (grid) {
        gridFound++;
        var kids = grid.children;
        for (var k = 0; k < kids.length; k++) {
          var lab = kids[k].children[0], val = kids[k].children[1];
          if (!lab || !val) continue;
          var L = lab.textContent.trim();
          if (L === 'Frequency') rec.frequency = val.textContent.trim();
          if (L === 'Last asked') rec.lastAsked = val.textContent.trim();
          if (L === 'Stage') rec.stage = val.textContent.trim();
        }
      }
      var hdr = main ? main.querySelector('header') : null;
      if (hdr) {
        var dm = (hdr.textContent || '').match(/(\d+)\s*min/);
        if (dm) rec.duration = dm[1] + ' min';
        var ht = hdr.textContent || '';
        if (ht.indexOf('Free preview') >= 0) rec.access = 'Free preview';
        else if (ht.indexOf('Members') >= 0) rec.access = 'Members';
      }
      rec.rawBody = main ? txt(main) : null;

      var as = doc.querySelectorAll('a');
      for (var j = 0; j < as.length; j++) {
        var h = as[j].getAttribute('href') || '';
        if (h.indexOf(PREFIX) !== 0) continue;
        var s = h.slice(PREFIX.length);
        if (!s || s.indexOf('/') >= 0) continue;
        if (!seen[PREFIX + s] && queue.indexOf(h) < 0) queue.push(h);
      }
    } catch (e) { rec.error = String(e); }
    recs.push(rec);
  }
  return JSON.stringify({ found: recs.length, gridFound: gridFound, stillQueued: queue.length, records: recs });
})()
