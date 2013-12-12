package com.youer.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.youer.R;
import com.youer.modal.ChatEmoji;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;

/**
 * 
 ****************************************** 
 * @author 寤栦箖娉� * @鏂囦欢鍚嶇О : FaceConversionUtil.java
 * @鍒涘缓鏃堕棿 : 2013-1-27 涓嬪崍02:34:09
 * @鏂囦欢鎻忚堪 : 琛ㄦ儏杞夋彌宸ュ叿
 ****************************************** 
 */
public class FaceConversionUtil {

	/** 姣忎竴椤佃〃鎯呯殑涓暟 */
	private int pageSize = 20;

	private static FaceConversionUtil mFaceConversionUtil;

	/** 淇濆瓨浜庡唴瀛樹腑鐨勮〃鎯匟ashMap */
	private HashMap<String, String> emojiMap = new HashMap<String, String>();

	/** 淇濆瓨浜庡唴瀛樹腑鐨勮〃鎯呴泦鍚�*/
	private List<ChatEmoji> emojis = new ArrayList<ChatEmoji>();

	/** 琛ㄦ儏鍒嗛〉鐨勭粨鏋滈泦鍚�*/
	public List<List<ChatEmoji>> emojiLists = new ArrayList<List<ChatEmoji>>();

	private FaceConversionUtil() {

	}

	public static FaceConversionUtil getInstace() {
		if (mFaceConversionUtil == null) {
			mFaceConversionUtil = new FaceConversionUtil();
		}
		return mFaceConversionUtil;
	}

	/**
	 * 寰楀埌涓�釜SpanableString瀵硅薄锛岄�杩囦紶鍏ョ殑瀛楃涓�骞惰繘琛屾鍒欏垽鏂�	 * 
	 * @param context
	 * @param str
	 * @return
	 */
	public SpannableString getExpressionString(Context context, String str) {
		SpannableString spannableString = new SpannableString(str);
		// 姝ｅ垯琛ㄨ揪寮忔瘮閰嶅瓧绗︿覆閲屾槸鍚﹀惈鏈夎〃鎯咃紝濡傦細 鎴戝ソ[寮�績]鍟�		S
		String zhengze = "\\[[^\\]]+\\]";
		// 閫氳繃浼犲叆鐨勬鍒欒〃杈惧紡鏉ョ敓鎴愪竴涓猵attern
		Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
		try {
			dealExpression(context, spannableString, sinaPatten, 0);
		} catch (Exception e) {
			Log.e("dealExpression", e.getMessage());
		}
		return spannableString;
	}

	/**
	 * 娣诲姞琛ㄦ儏
	 * 
	 * @param context
	 * @param imgId
	 * @param spannableString
	 * @return
	 */
	public SpannableString addFace(Context context, int imgId,
			String spannableString) {
		if (TextUtils.isEmpty(spannableString)) {
			return null;
		}
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				imgId);
		bitmap = Bitmap.createScaledBitmap(bitmap, 35, 35, true);
		ImageSpan imageSpan = new ImageSpan(context, bitmap);
		SpannableString spannable = new SpannableString(spannableString);
		spannable.setSpan(imageSpan, 0, spannableString.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	/**
	 * 瀵箂panableString杩涜姝ｅ垯鍒ゆ柇锛屽鏋滅鍚堣姹傦紝鍒欎互琛ㄦ儏鍥剧墖浠ｆ浛
	 * 
	 * @param context
	 * @param spannableString
	 * @param patten
	 * @param start
	 * @throws Exception
	 */
	private void dealExpression(Context context,
			SpannableString spannableString, Pattern patten, int start)
			throws Exception {
		Matcher matcher = patten.matcher(spannableString);
		while (matcher.find()) {
			String key = matcher.group();
			// 杩斿洖绗竴涓瓧绗︾殑绱㈠紩鐨勬枃鏈尮閰嶆暣涓鍒欒〃杈惧紡,ture 鍒欑户缁�褰�			
			if (matcher.start() < start) {
				continue;
			}
			String value = emojiMap.get(key);
			if (TextUtils.isEmpty(value)) {
				continue;
			}
			int resId = context.getResources().getIdentifier(value, "drawable",
					context.getPackageName());
			// 閫氳繃涓婇潰鍖归厤寰楀埌鐨勫瓧绗︿覆鏉ョ敓鎴愬浘鐗囪祫婧恑d
			// Field field=R.drawable.class.getDeclaredField(value);
			// int resId=Integer.parseInt(field.get(null).toString());
			if (resId != 0) {
				Bitmap bitmap = BitmapFactory.decodeResource(
						context.getResources(), resId);
				bitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
				// 閫氳繃鍥剧墖璧勬簮id鏉ュ緱鍒癰itmap锛岀敤涓�釜ImageSpan鏉ュ寘瑁�				
				ImageSpan imageSpan = new ImageSpan(bitmap);
				// 璁＄畻璇ュ浘鐗囧悕瀛楃殑闀垮害锛屼篃灏辨槸瑕佹浛鎹㈢殑瀛楃涓茬殑闀垮害
				int end = matcher.start() + key.length();
				// 灏嗚鍥剧墖鏇挎崲瀛楃涓蹭腑瑙勫畾鐨勪綅缃腑
				spannableString.setSpan(imageSpan, matcher.start(), end,
						Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				if (end < spannableString.length()) {
					// 濡傛灉鏁翠釜瀛楃涓茶繕鏈獙璇佸畬锛屽垯缁х画銆傘�
					dealExpression(context, spannableString, patten, end);
				}
				break;
			}
		}
	}

	public void getFileText(Context context) {
		ParseData(FileUtils.getEmojiFile(context), context);
	}

	/**
	 * 瑙ｆ瀽瀛楃
	 * 
	 * @param data
	 */
	private void ParseData(List<String> data, Context context) {
		if (data == null) {
			return;
		}
		ChatEmoji emojEentry;
		try {
			for (String str : data) {
				String[] text = str.split(",");
				String fileName = text[0]
						.substring(0, text[0].lastIndexOf("."));
				emojiMap.put(text[1], fileName);
				int resID = context.getResources().getIdentifier(fileName,
						"drawable", context.getPackageName());

				if (resID != 0) {
					emojEentry = new ChatEmoji();
					emojEentry.setId(resID);
					emojEentry.setCharacter(text[1]);
					emojEentry.setFaceName(fileName);
					emojis.add(emojEentry);
				}
			}
			int pageCount = (int) Math.ceil(emojis.size() / 20 + 0.1);

			for (int i = 0; i < pageCount; i++) {
				emojiLists.add(getData(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 鑾峰彇鍒嗛〉鏁版嵁
	 * 
	 * @param page
	 * @return
	 */
	private List<ChatEmoji> getData(int page) {
		int startIndex = page * pageSize;
		int endIndex = startIndex + pageSize;

		if (endIndex > emojis.size()) {
			endIndex = emojis.size();
		}
		// 涓嶈繖涔堝啓锛屼細鍦╲iewpager鍔犺浇涓姤闆嗗悎鎿嶄綔寮傚父锛屾垜涔熶笉鐭ラ亾涓轰粈涔�		
		List<ChatEmoji> list = new ArrayList<ChatEmoji>();
		list.addAll(emojis.subList(startIndex, endIndex));
		if (list.size() < pageSize) {
			for (int i = list.size(); i < pageSize; i++) {
				ChatEmoji object = new ChatEmoji();
				list.add(object);
			}
		}
		if (list.size() == pageSize) {
			ChatEmoji object = new ChatEmoji();
			object.setId(R.drawable.face_del_icon);
			list.add(object);
		}
		return list;
	}
}