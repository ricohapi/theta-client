'use strict';

/* global Marzipano */

let _imageAsset = null;

function createImageAsset() {
  let imageTag = document.createElement('img');
  imageTag.crossOrigin = 'anonymous';
  let asset = new Marzipano.DynamicAsset(imageTag);
  return asset;
}

// eslint-disable-next-line no-unused-vars
function setFrame(frameData) {
  if (_imageAsset == null || _imageAsset.element() == null) {
    return;
  }

  let element = _imageAsset.element();
  if (element.onload) {
    return;
  }
  element.onload = () => {
    _imageAsset.markDirty();
    element.onload = null;
  };
  element.onerror = () => {
    element.onload = null;
  };

  element.src = frameData;
}

// Create viewer.
var viewer = new Marzipano.Viewer(document.getElementById('pano'));

// Create source.
_imageAsset = createImageAsset();
const source = new Marzipano.SingleAssetSource(_imageAsset);

// Create geometry.
var geometry = new Marzipano.EquirectGeometry([{width: 4000}]);

// Create view.
var limiter = Marzipano.RectilinearView.limit.traditional(
  1024,
  (100 * Math.PI) / 180,
);
var view = new Marzipano.RectilinearView({yaw: Math.PI}, limiter);

// Create scene.
var scene = viewer.createScene({
  source: source,
  geometry: geometry,
  view: view,
  pinFirstLevel: true,
});

// Display scene.
scene.switchTo();
