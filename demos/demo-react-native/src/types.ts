export type PhotoItem = {
    fileUrl: string;
    name: string;
};

export type Navigation = {
    navigate: (screen: string, params?: any) => void;
    goBack: () => void;
    setOptions: (options: { title: string }) => void;
};

