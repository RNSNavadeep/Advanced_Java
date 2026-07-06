void main()
{
    var list=[{"Name":"John","Age":30},{"Name":"Jane","Age":25}];
    for(var i=0;i<list.length;i++)
    {var x=list[i];
    print("name=${x['Name']},age=${x['Age']}");
    }
}