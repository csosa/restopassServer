package restopass.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import restopass.dto.MembershipType;

@ReadingConverter
public class IntegerToEnumConverter implements Converter<Integer, MembershipType> {

    @Override
    public MembershipType convert(Integer integer) {
        if(MembershipType.BASIC.ordinal() == integer) {
            return MembershipType.BASIC;
        }

        if(MembershipType.GOLD.ordinal() == integer) {
            return MembershipType.GOLD;
        }

        if(MembershipType.PLATINUM.ordinal() == integer) {
            return MembershipType.PLATINUM;
        }

        return MembershipType.BASIC;
    }
}
