package hieuboy.developer.utils;

public class Common {

    /**
     * Quy tắc gen mã checksum:
     * Mã khách hàng là số tự sinh được cộng thêm số '0' cho đủ 8 kí tự
     * ->    Nhân giá trị các số hạng của dãy số đã nhập với các số hạng tương ứng của dãy số phụ thêm sau: 8,6,4,2,3,5,9,7
     * ->    Tính tổng của các kết quả vừa thực hiện
     * ->    Chia giá trị tổng trên cho 11
     * ->    Lấy 11 trừ đi số dư của phép chia vừa thực hiện
     * ->    Nếu kết quả nằm trong dãy từ 1 đến 9 thì sử dụng kết quả đó làm số kiểm tra
     * ->    Nếu kết quả là 10 thì lấy số 0 làm số kiểm tra
     * ->    Nếu kết quả là sô 11 thì lấy số 5 làm số kiểm tra
     *
     * @param codeCheck
     * @return
     */

    public static String genCheckSumCode(String codeCheck) {
        int[] arraySoPhu = new int[]{8, 6, 4, 2, 3, 5, 9, 7};
        int sum = 0;
        int soDu = 0;
        for (int i = 0; i < arraySoPhu.length; i++)
            sum += arraySoPhu[i] * Integer.parseInt(codeCheck.split("")[i]);
        soDu = sum % 11;
        int soKiemTra = (11 - soDu < 10) ? 11 - soDu : (11 - soDu == 10 ? 0 : 5);
        return Integer.toString(soKiemTra);
    }

}
