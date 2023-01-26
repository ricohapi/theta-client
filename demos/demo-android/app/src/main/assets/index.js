/* eslint-disable no-console */
/* eslint-disable no-nested-ternary */
const { ViewerContainer } = window.RICOH360Viewer;
const { rxjs } = window;
const { operators } = rxjs;

const sceneId = 'sceneEqui';
const getImageUrl = (id) => {
    return injectedObject.getPhotoUrl()
};

const getScene = async (id) => {
  return {
    id,
    image: {
      type: 'equirect',
      url: getImageUrl(id),
      viewParams: { yaw: 0, pitch: 0, fov: 2.4 },
    }
  }
};

const eventHandler = {
  onSceneChange: (id) => {
    console.log(`Change scene: ${id}`);
  },
  onClick: (x) => console.log(`Click ${x}`),
  onTextureLoad: () => console.log('Texture loaded'),
  onViewChanged: (x) => console.log(`View changed: ${JSON.stringify(x)}`),
};

const divElement = document.getElementById('root');
const viewer = new ViewerContainer({
  div: divElement,
  config: {
    color: {
      background: 'navy',
    },
    controls: {
      scrollZoom: true,
    },
    scene: {
      numOfCache: 3,
    },
    transitionDuration: 5, // millisecond
    view: {
      keepCoords: true,
      limit3D: {
        maxVFov: 2.8, // radian
        minVFov: 0.8,
        maxHFov: 2.8,
      },
    },
  },
  getScene,
});
viewer.viewerCreated$.subscribe(({ index }) =>
  console.log(`Viewer created: ${index}`),
);
viewer.viewerCreated$.subscribe(({ viewer: v, index }) => {
  v.sceneId$.subscribe(eventHandler.onSceneChange);
  v.viewCoords$
    .pipe(operators.throttleTime(500))
    .subscribe(eventHandler.onViewChanged);
  v.clicked$.subscribe(eventHandler.onClick);
  v.clicked$
    .pipe(operators.switchMap((x) => v.coordinatesToScreen(x)))
    .subscribe(console.log);
  v.textureLoaded$.subscribe(eventHandler.onTextureLoad);
  v.isActive$.subscribe((x) => console.log(`isActive: ${x}`));

  v.switchScene(sceneId);
});

viewer.addNewViewer();