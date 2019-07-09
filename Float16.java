/**
 * 16 bit implementation of the Java Float data type.
 * Decimal precision of up to three and a half digits.
 * @author Jacob Burton
 *
 */
public class Float16 {

	/**
	 * Packs a 32 bit float value into a 16 bit byte array
	 */
	public static void pack(float f, byte[] array) throws Exception {
		
		// Check length of byte array
		if(array.length != 2) throw new Exception("Byte array must be a length of 2.");
		
		// Zero out byte array
		array[0] = 0;
		array[1] = 0;
		
		// Split float into whole digit, decimal part, and rounding part
		short whole = (short) f;
		short part = (short) ((f - whole) * 1000);
		short round = (short) (((f - whole) * 10000) - (part * 10));
		
		// Check if whole value is greater than maximum
		if((whole & 0xf) != whole) {
			throw new Exception("Range of whole ("+whole+") value must be between 0 and 15.");
		}
		
		// Pack whole number into the first four bits
		array[0] = (byte) whole;
		
		// Pack part into next ten bits
		array[0] |= (part & 0xf) << 4;
		array[1] = (byte) ((part & 0x3f0) >> 4);
		
		// Pack round into the last two bits
		array[1] |= (byte) (((round + 1) / 3) << 6);
		
	}

	/**
	 * Unpacks a 16 bit byte array into a 32 bit float value
	 */
	public static float unpack(byte[] array) {
		
		short whole = (short) (array[0] & 0xf);
		short part = (short) (((array[0] & 0xf0 ) >> 4) | ((array[1] & 0x3f) << 4));
		short round = (short) ((array[1] & 0xc0) >> 6);
		
		float f = (float) whole + ((float) part / 1000) + (((float) round * 3) / 10000);
		
		return f;
	}
	
}
