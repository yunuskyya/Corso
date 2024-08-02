import { useAppSelector } from "../redux/hooks";

const useAuth = () => {
    const user = useAppSelector((state) => state.auth.user);

    return {
        user,
        isAuthenticated: !!user,
        isAdmin: user?.authorities[0] === 'admin',
    };
};

export default useAuth;
